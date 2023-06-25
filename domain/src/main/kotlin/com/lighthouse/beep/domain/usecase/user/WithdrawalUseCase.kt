package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId()
        authRepository.withdrawal().getOrThrow()
        authRepository.signOut().getOrThrow()
        userRepository.withdrawal(userId).getOrThrow()
    }
}
