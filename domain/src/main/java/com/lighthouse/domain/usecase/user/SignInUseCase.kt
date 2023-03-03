package com.lighthouse.domain.usecase.user

import com.google.firebase.auth.AuthCredential
import com.lighthouse.beep.model.auth.AuthProvider
import com.lighthouse.domain.repository.auth.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(provider: AuthProvider, credential: AuthCredential): Result<Unit> {
        return authRepository.signIn(provider, credential)
    }
}