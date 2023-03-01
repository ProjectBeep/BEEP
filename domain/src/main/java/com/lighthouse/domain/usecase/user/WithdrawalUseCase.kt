package com.lighthouse.domain.usecase.user

import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId()
        authRepository.withdrawal().getOrThrow()
        userRepository.withdrawal(userId).getOrThrow()
    }
}
