package com.lighthouse.beep.auth.repository

import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.model.OAuthRequest
import com.lighthouse.beep.auth.model.exception.InvalidUserException
import com.lighthouse.beep.auth.service.oauth.OAuthServiceProvider
import com.lighthouse.beep.auth.service.signout.SignOutServiceProvider
import com.lighthouse.beep.model.deviceconfig.AuthInfo
import com.lighthouse.beep.model.deviceconfig.AuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class AuthRepositoryImpl @Inject constructor(
    private val oAuthServiceProvider: OAuthServiceProvider,
    private val signOutServiceProvider: SignOutServiceProvider,
) : AuthRepository {

    override val authInfo: Flow<AuthInfo> = callbackFlow {
        val authStateListener = AuthStateListener {
            val user = it.currentUser
            if (user == null) {
                trySend(AuthInfo(provider = AuthProvider.NONE))
            } else {
                val provider = runCatching {
                    if (user.isAnonymous) {
                        AuthProvider.GUEST
                    } else {
                        val result = user.getIdToken(true).result
                        val providerName = result.claims["provider"] as? String ?: ""

                        AuthProvider.of(providerName)
                    }
                }.getOrDefault(AuthProvider.NONE)

                val info = AuthInfo(
                    userUid = user.uid,
                    provider = provider,
                    displayName = user.displayName ?: "",
                    email = user.email ?: "",
                    photoUrl = user.photoUrl,
                )
                trySend(info)
            }
        }

        Firebase.auth.addAuthStateListener(authStateListener)
        awaitClose {
            Firebase.auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signIn(request: OAuthRequest) {
        val service = oAuthServiceProvider.getOAuthService(request.provider)
        val task = service.signIn(request)

        suspendCancellableCoroutine { continuation ->
            task.addOnSuccessListener {
                continuation.resume(Unit)
            }
            task.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun signOut() {
        val provider = authInfo.first().provider
        signOutServiceProvider.getSignOutService(provider).signOut()
        Firebase.auth.signOut()
    }

    override suspend fun withdrawal() {
        val provider = authInfo.first().provider
        signOutServiceProvider.getSignOutService(provider).signOut()
        val user = Firebase.auth.currentUser ?: throw InvalidUserException()
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
