package com.lighthouse.auth.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.model.auth.AuthProvider
import com.lighthouse.beep.model.auth.exception.InvalidUserException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val serviceProvider: OAuthServiceProvider,
) : AuthRepository {

    override fun isLogin(): Flow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser == null)
        }
        Firebase.auth.addAuthStateListener(authStateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isGuest(): Flow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser == null || it.currentUser?.isAnonymous == true)
        }
        Firebase.auth.addAuthStateListener(authStateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun getCurrentUserId(): String {
        return Firebase.auth.currentUser?.uid ?: ""
    }

    suspend fun signIn(provider: AuthProvider, credential: AuthCredential): Result<Unit> {
        val service = serviceProvider.getOAuthService(provider)
        return runCatching {
            service.signIn(credential)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        val user = Firebase.auth.currentUser ?: return Result.failure(InvalidUserException())
        return runCatching {
            when (user.providerId) {
//                GoogleAuthProvider.PROVIDER_ID -> googleOAuthService.signOut()
            }
            Firebase.auth.signOut()
        }
    }

    override suspend fun withdrawal(): Result<Unit> {
        val user = Firebase.auth.currentUser ?: return Result.failure(InvalidUserException())
        return runCatching {
            when (user.providerId) {
//                GoogleAuthProvider.PROVIDER_ID -> googleOAuthService.signOut()
            }
            callbackFlow {
                user.delete().addOnCompleteListener {
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
