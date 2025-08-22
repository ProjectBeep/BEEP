package com.lighthouse.beep.auth.client

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.auth.model.OAuthTokenResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class GoogleClient(context: Context) {
    private val client by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val signInIntent
        get() = client.signInIntent

    suspend fun getAccessToken(result: ActivityResult): OAuthTokenResult {
        return when (result.resultCode) {
            Activity.RESULT_OK -> {
                runCatching {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data).await()
                    val token = task?.idToken
                    if (token.isNullOrEmpty()) {
                        throw NullPointerException("Token is Null!")
                    } else {
                        OAuthTokenResult.Success(token)
                    }
                }.getOrElse {
                    OAuthTokenResult.Failed(it)
                }
            }

            Activity.RESULT_CANCELED -> OAuthTokenResult.Canceled()
            else -> {
                OAuthTokenResult.Failed(IllegalStateException("구글 로그인에 실패 했습니다."))
            }
        }
    }

    suspend fun signOut() {
        client.signOut().await()
    }
}
