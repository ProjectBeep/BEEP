package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.domain.repository.user.UserRepository
import com.lighthouse.beep.model.user.SecurityOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSecurityOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Result<SecurityOption>> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.getSecurityOption(userId)
    }
}
