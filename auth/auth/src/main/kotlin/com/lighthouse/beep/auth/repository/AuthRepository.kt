package com.lighthouse.beep.auth.repository

import com.lighthouse.beep.auth.model.OAuthRequest
import com.lighthouse.beep.model.user.AuthInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authInfo: Flow<AuthInfo>

    suspend fun signIn(request: OAuthRequest): AuthInfo

    suspend fun signOut()

    suspend fun withdrawal()
}
