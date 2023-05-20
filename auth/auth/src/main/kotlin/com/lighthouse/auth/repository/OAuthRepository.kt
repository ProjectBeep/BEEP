package com.lighthouse.auth.repository

import com.google.firebase.auth.AuthCredential

interface OAuthRepository {

    suspend fun signIn(credential: AuthCredential): Result<Unit>

    suspend fun signOut(): Result<Unit>
}
