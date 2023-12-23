package com.lighthouse.beep.auth.client

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.auth.model.OAuthTokenResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class KakaoClient(context: Context) {

    init {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    suspend fun getAccessToken(context: Context): OAuthTokenResult {
        return withContext(Dispatchers.IO) {
            if (AuthApiClient.instance.hasToken()) {
                runCatching {
                    getAccessTokenWithCurrent()
                }.getOrElse { error ->
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        getAccessTokenWithLogin(context)
                    } else {
                        OAuthTokenResult.Failed(error)
                    }
                }
            } else {
                getAccessTokenWithLogin(context)
            }
        }
    }

    private suspend fun getAccessTokenWithCurrent(): OAuthTokenResult {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    throw error
                } else {
                    val manager = AuthApiClient.instance.tokenManagerProvider.manager
                    val accessToken = manager.getToken()?.accessToken
                    if (accessToken.isNullOrEmpty()) {
                        throw NullPointerException("Token is Null!")
                    } else {
                        continuation.resume(OAuthTokenResult.Success(accessToken))
                    }
                }
            }
        }
    }

    private suspend fun getAccessTokenWithLogin(context: Context): OAuthTokenResult {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            getAccessTokenWithKakaoTalk(context)
        } else {
            getAccessTokenWithKakaoAccount(context)
        }
    }

    private fun getAccessTokenWithLogin(
        token: OAuthToken?,
        error: Throwable?,
    ): OAuthTokenResult {
        return if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                OAuthTokenResult.Canceled()
            } else {
                OAuthTokenResult.Failed(error)
            }
        } else {
            val accessToken = token?.accessToken
            if (accessToken.isNullOrEmpty()) {
                OAuthTokenResult.Failed(NullPointerException("Token is Null!"))
            } else {
                OAuthTokenResult.Success(accessToken)
            }
        }
    }

    private suspend fun getAccessTokenWithKakaoTalk(context: Context): OAuthTokenResult {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                continuation.resume(getAccessTokenWithLogin(token, error))
            }
        }
    }

    private suspend fun getAccessTokenWithKakaoAccount(context: Context): OAuthTokenResult {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                continuation.resume(getAccessTokenWithLogin(token, error))
            }
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
