package com.lighthouse.beep.auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.lighthouse.beep.auth.model.OAuthRequest
import com.lighthouse.beep.auth.model.exception.InvalidUserException
import com.lighthouse.beep.auth.service.oauth.OAuthServiceProvider
import com.lighthouse.beep.auth.service.signout.SignOutServiceProvider
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val oAuthServiceProvider: OAuthServiceProvider,
    private val signOutServiceProvider: SignOutServiceProvider,
) : AuthRepository {

    private fun FirebaseUser?.toAuthInfo(): AuthInfo {
        this ?: return AuthInfo(provider = AuthProvider.NONE)

        val provider = runCatching {
            if (isAnonymous) {
                AuthProvider.GUEST
            } else {
                val result = getIdToken(true).result
                val providerName = result.claims["provider"] as? String ?: ""

                AuthProvider.of(providerName)
            }
        }.getOrDefault(AuthProvider.NONE)

        return AuthInfo(
            userUid = uid,
            provider = provider,
            displayName = displayName ?: "",
            email = email ?: "",
            photoUrl = photoUrl,
        )
    }

    override val authInfo: Flow<AuthInfo> = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser.toAuthInfo())
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signIn(request: OAuthRequest): AuthInfo {
        val service = oAuthServiceProvider.getOAuthService(request.provider)
        val task = service.signIn(request)

        return suspendCancellableCoroutine { continuation ->
            task.addOnSuccessListener {
                continuation.resume(it.user.toAuthInfo())
            }
            task.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun signOut() {
        val provider = authInfo.first().provider
        signOutServiceProvider.getSignOutService(provider).signOut()
        firebaseAuth.signOut()
    }

    override suspend fun withdrawal() {
        val provider = authInfo.first().provider
        signOutServiceProvider.getSignOutService(provider).signOut()
        val user = firebaseAuth.currentUser ?: throw InvalidUserException()
        val task = user.delete()
        suspendCancellableCoroutine { continuation ->
            task.addOnSuccessListener {
                continuation.resume(Unit)
            }
            task.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}
