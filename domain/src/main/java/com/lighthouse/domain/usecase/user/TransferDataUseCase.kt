package com.lighthouse.domain.usecase.user

import com.lighthouse.beep.model.auth.exception.InvalidUserException
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class TransferDataUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(oldUserId: String): Result<Unit> {
        return runCatching {
            val newUserId = authRepository.getCurrentUserId()
            userRepository.transferData(oldUserId, newUserId).getOrThrow()
        }
    }
}
