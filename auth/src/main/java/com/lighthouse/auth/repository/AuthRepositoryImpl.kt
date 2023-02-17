package com.lighthouse.auth.repository

import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.domain.repository.auth.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val isGuestFlow = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser == null)
        }

        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isGuest(): Flow<Boolean> {
        return isGuestFlow
    }

    override fun getCurrentUserId(): String {
        return Firebase.auth.currentUser?.uid ?: GUEST_ID
    }

    companion object {
        private const val GUEST_ID = "Guest"
    }
}
