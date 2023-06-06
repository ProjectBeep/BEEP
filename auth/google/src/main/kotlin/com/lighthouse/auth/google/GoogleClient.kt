package com.lighthouse.auth.google

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lighthouse.beep.auth.google.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val client by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val signInIntent
        get() = client.signInIntent

    suspend fun getAccessToken(result: ActivityResult): GoogleTokenResult {
        return when (result.resultCode) {
            Activity.RESULT_OK -> {
                runCatching {
                    suspendCancellableCoroutine<GoogleTokenResult> { continuation ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        task.addOnSuccessListener {
                            val token = it.idToken
                            if (token != null) {
                                continuation.resume(GoogleTokenResult.Success(token))
                            } else {
                                continuation.resumeWithException(NullPointerException("Token is Null!"))
                            }
                        }
                        task.addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                    }
                }.getOrElse {
                    GoogleTokenResult.Failed(it)
                }
            }

            Activity.RESULT_CANCELED -> GoogleTokenResult.Canceled
            else -> {
                GoogleTokenResult.Failed(IllegalStateException("구글 로그인에 실패 했습니다."))
            }
        }
    }

    suspend fun signOut() {
        suspendCancellableCoroutine { continuation ->
            val task = client.signOut()
            task.addOnSuccessListener {
                continuation.resume(Unit)
            }
            task.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}
