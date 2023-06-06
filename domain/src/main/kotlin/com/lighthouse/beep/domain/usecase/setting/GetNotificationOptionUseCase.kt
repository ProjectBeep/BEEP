package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Result<Boolean>> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.getNotificationEnable(userId)
    }
}
