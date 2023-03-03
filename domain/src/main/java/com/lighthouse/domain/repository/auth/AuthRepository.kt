package com.lighthouse.domain.repository.auth

import com.google.firebase.auth.AuthCredential
import com.lighthouse.beep.model.auth.AuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isLogin(): Flow<Boolean>

    fun isGuest(): Flow<Boolean>

    fun getCurrentUserId(): String

    suspend fun signIn(provider: AuthProvider, credential: AuthCredential): Result<Unit>

    suspend fun signOut(): Result<Unit>

    suspend fun withdrawal(): Result<Unit>
}
