package com.lighthouse.beep.auth.repository

import com.lighthouse.beep.auth.model.OAuthRequest

interface AuthRepository {

    fun getCurrentUserId(): String

    suspend fun signIn(request: OAuthRequest): Result<Unit>

    suspend fun signOut(): Result<Unit>

    suspend fun withdrawal(): Result<Unit>
}
