package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
}
