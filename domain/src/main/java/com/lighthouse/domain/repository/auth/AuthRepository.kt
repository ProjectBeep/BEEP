package com.lighthouse.domain.repository.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isGuest(): Flow<Boolean>

    fun getCurrentUserId(): String

    suspend fun withdrawal(): Result<Unit>
}
