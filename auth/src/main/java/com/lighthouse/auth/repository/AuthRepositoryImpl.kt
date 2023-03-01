package com.lighthouse.auth.repository

import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.domain.repository.auth.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override fun isGuest(): Flow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser == null)
        }
        Firebase.auth.addAuthStateListener(authStateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun getCurrentUserId(): String {
        return Firebase.auth.currentUser?.uid ?: GUEST_ID
    }

    override suspend fun withdrawal(): Result<Unit> {
        val user = Firebase.auth.currentUser ?: return Result.success(Unit)
        return runCatching {
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

    companion object {
        private const val GUEST_ID = "Guest"
    }
}
