package com.lighthouse.auth.google.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.auth.google.R
import com.lighthouse.auth.google.exception.FailedConnectException
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
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun signInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    private suspend fun getGoogleAccount(result: ActivityResult): GoogleSignInAccount =
        callbackFlow {
            GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(it.result)
                }
                close(it.exception)
            }
            awaitClose()
        }.first()

    private suspend fun signInWithAccount(account: GoogleSignInAccount): AuthResult {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return callbackFlow {
            Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(it.result)
                }
                close(it.exception)
            }
            awaitClose()
        }.first()
    }

    override suspend fun signIn(result: ActivityResult): Result<AuthResult> {
        return if (result.resultCode == Activity.RESULT_OK) {
            runCatching {
                val account = getGoogleAccount(result)
                signInWithAccount(account)
            }
        } else {
            Result.failure(FailedConnectException())
        }
    }

    override suspend fun signOut(): Result<Unit> {
        Firebase.auth.currentUser ?: return Result.success(Unit)
        return runCatching {
            callbackFlow {
                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(Unit)
                    }
                    close(it.exception)
                }
                awaitClose()
            }.first()
        }
    }
}
