package com.lighthouse.domain.usecase.setting

import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class SetSecurityOptionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(options: SecurityOption): Result<Unit> {
        val userId = authRepository.getCurrentUserId()
        return userRepository.setSecurityOption(userId, options)
    }
}
