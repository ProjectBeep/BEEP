package com.lighthouse.auth.google.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.auth.repository.OAuthRepository
import com.lighthouse.beep.auth.google.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class GoogleOAuthRepository @Inject constructor(
    @ApplicationContext context: Context,
) : OAuthRepository, GoogleClient {

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

    override suspend fun getAuthCredential(result: ActivityResult): Result<AuthCredential> {
        return if (result.resultCode == Activity.RESULT_OK) {
            runCatching {
                val account = getGoogleAccount(result)
                GoogleAuthProvider.getCredential(account.idToken, null)
            }
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun signIn(credential: AuthCredential): Result<Unit> {
        return runCatching {
            callbackFlow {
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(Unit)
                    }
                    close(it.exception)
                }
                awaitClose()
            }.first()
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
