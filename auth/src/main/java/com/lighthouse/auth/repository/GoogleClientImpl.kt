package com.lighthouse.auth.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.auth.R
import com.lighthouse.auth.exception.FailedApiException
import com.lighthouse.auth.exception.FailedConnectException
import com.lighthouse.auth.exception.FailedLoginException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class GoogleClientImpl @Inject constructor(
    @ApplicationContext context: Context
) : GoogleClient {

    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun googleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    private suspend fun signInWithAccount(account: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return callbackFlow {
            Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                trySend(it)
                close()
            }
            awaitClose()
        }.first()
    }

    override suspend fun googleSignIn(result: ActivityResult): Result<AuthResult> {
        return if (result.resultCode == Activity.RESULT_OK) {
            runCatching {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                signInWithAccount(task.getResult(FailedApiException::class.java))
                    .getResult(FailedLoginException::class.java)
            }
        } else {
            Result.failure(FailedConnectException())
        }
    }
}
