package com.lighthouse.beep.auth.mapper

import com.google.firebase.auth.FirebaseUser
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.tasks.await

internal suspend fun FirebaseUser?.toAuthInfo(): AuthInfo {
    this ?: return AuthInfo(provider = AuthProvider.NONE)

    val provider = runCatching {
        val result = getIdToken(true).await()
        if (isAnonymous) {
            AuthProvider.GUEST
        } else {
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