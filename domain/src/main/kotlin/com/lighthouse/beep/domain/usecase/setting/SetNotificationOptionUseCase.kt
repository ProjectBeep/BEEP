package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.domain.repository.user.UserRepository
import javax.inject.Inject

class SetNotificationOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(enable: Boolean): Result<Unit> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.setNotificationEnable(userId, enable)
    }
}
