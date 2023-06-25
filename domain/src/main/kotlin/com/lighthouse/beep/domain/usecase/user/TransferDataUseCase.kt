package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import javax.inject.Inject

class TransferDataUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(oldUserId: String): Result<Unit> {
        return runCatching {
            val newUserId = authRepository.getCurrentUserId()
            userRepository.transferData(oldUserId, newUserId).getOrThrow()
        }
    }
}
