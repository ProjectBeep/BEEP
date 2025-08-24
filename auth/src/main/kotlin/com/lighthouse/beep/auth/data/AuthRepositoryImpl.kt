package com.lighthouse.beep.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface GifticonRepository {
    suspend fun deleteGifticon(userUid: String)
}

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val gifticonRepository: GifticonRepository,
) : AuthRepository {
    
    override val authInfoFlow: Flow<AuthInfo?> = BeepAuth.authInfoFlow
    
    override val currentAuthInfo: AuthInfo?
        get() = BeepAuth.authInfo
    
    override suspend fun signInWithCustomToken(provider: AuthProvider, accessToken: String) {
        val firebaseToken = remoteAuthDataSource.getCustomToken(provider, accessToken)
        Firebase.auth.signInWithCustomToken(firebaseToken).await()
    }
    
    override suspend fun signInAsGuest() {
        val auth = FirebaseAuth.getInstance()
        val authResult = auth.signInAnonymously().await()
        
        BeepAuth.updateProfile(userProfileChangeRequest {
            displayName = "게스트"
        })
    }
    
    override suspend fun signOut() {
        Firebase.auth.signOut()
    }
    
    override suspend fun updateDisplayName(displayName: String) {
        BeepAuth.updateProfile(userProfileChangeRequest {
            this.displayName = displayName
        })
    }
    
    override suspend fun withdrawalAccount() {
        val userUid = BeepAuth.userUid
        gifticonRepository.deleteGifticon(userUid)
        Firebase.auth.currentUser?.delete()?.await()
    }
}