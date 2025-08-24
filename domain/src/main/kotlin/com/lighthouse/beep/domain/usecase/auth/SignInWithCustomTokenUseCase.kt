package com.lighthouse.beep.domain.usecase.auth

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.model.user.AuthProvider
import javax.inject.Inject

class SignInWithCustomTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    
    suspend operator fun invoke(provider: AuthProvider, accessToken: String) {
        authRepository.signInWithCustomToken(provider, accessToken)
    }
}