package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.domain.repository.user.UserRepository
import com.lighthouse.beep.model.user.SecurityOption
import javax.inject.Inject

class SetSecurityOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(options: SecurityOption): Result<Unit> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.setSecurityOption(userId, options)
    }
}
