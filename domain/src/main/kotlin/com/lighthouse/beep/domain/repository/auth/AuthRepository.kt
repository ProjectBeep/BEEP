package com.lighthouse.beep.domain.repository.auth

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    
    val authInfoFlow: Flow<AuthInfo?>
    
    val currentAuthInfo: AuthInfo?
    
    suspend fun signInWithCustomToken(provider: AuthProvider, accessToken: String)
    
    suspend fun signInAsGuest()
    
    suspend fun signOut()
    
    suspend fun updateDisplayName(displayName: String)
    
    suspend fun withdrawalAccount()
}