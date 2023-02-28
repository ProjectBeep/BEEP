package com.lighthouse.domain.usecase.setting

import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class SetNotificationOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(enable: Boolean): Result<Unit> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.setNotificationEnable(userId, enable)
    }
}
