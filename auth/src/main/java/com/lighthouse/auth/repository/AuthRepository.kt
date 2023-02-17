package com.lighthouse.auth.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isGuest(): Flow<Boolean>

    fun getCurrentUserId(): String
}
