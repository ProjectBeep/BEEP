package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

//    suspend operator fun invoke(provider: AuthProvider, credential: AuthCredential): Result<Unit> {
//        return authRepository.signIn(provider, credential)
//    }
}
