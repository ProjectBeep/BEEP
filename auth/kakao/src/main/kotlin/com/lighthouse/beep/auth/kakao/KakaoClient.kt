package com.lighthouse.beep.auth.kakao

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.lighthouse.beep.auth.model.OAuthTokenResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KakaoClient @Inject constructor(
    @ApplicationContext context: Context,
) {

    init {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    suspend fun getAccessToken(context: Context): OAuthTokenResult {
        return withContext(Dispatchers.IO) {
            if (AuthApiClient.instance.hasToken()) {
                runCatching {
                    suspendCancellableCoroutine { continuation ->
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            if (error != null) {
                                continuation.resumeWithException(error)
                            } else {
                                val manager = AuthApiClient.instance.tokenManagerProvider.manager
                                val accessToken = manager.getToken()?.accessToken
                                if (accessToken != null) {
                                    continuation.resume(OAuthTokenResult.Success(accessToken))
                                } else {
                                    continuation.resumeWithException(NullPointerException("Token is Null!"))
                                }
                            }
                        }
                    }
                }.getOrElse {
                    OAuthTokenResult.Failed(it)
                }
            } else {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    getAccessTokenWithKakaoTalk(context)
                } else {
                    getAccessTokenWithKakaoAccount(context)
                }
            }
        }
    }

    private fun getAccessTokenWithLogin(
        continuation: CancellableContinuation<OAuthTokenResult>,
        token: OAuthToken?,
        error: Throwable?,
    ) {
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                continuation.resume(OAuthTokenResult.Canceled())
            } else {
                continuation.resumeWithException(error)
            }
        } else {
            val accessToken = token?.accessToken
            if (accessToken != null) {
                continuation.resume(OAuthTokenResult.Success(accessToken))
            } else {
                continuation.resumeWithException(NullPointerException("Token is Null!"))
            }
        }
    }

    private suspend fun getAccessTokenWithKakaoTalk(context: Context): OAuthTokenResult {
        return runCatching {
            suspendCancellableCoroutine { continuation ->
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    getAccessTokenWithLogin(continuation, token, error)
                }
            }
        }.getOrElse {
            OAuthTokenResult.Failed(it)
        }
    }

    private suspend fun getAccessTokenWithKakaoAccount(context: Context): OAuthTokenResult {
        return runCatching {
            suspendCancellableCoroutine { continuation ->
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    getAccessTokenWithLogin(continuation, token, error)
                }
            }
        }.getOrElse {
            OAuthTokenResult.Failed(it)
        }
    }

    suspend fun signOut(): Result<Unit> = runCatching {
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.logout { exception ->
                if (exception == null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(exception)
                }
            }
        }
    }
}
