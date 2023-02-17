package com.lighthouse.domain.usecase.user

import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.login(userId)
    }
}
