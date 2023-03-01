package com.lighthouse.domain.usecase.user

import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        userRepository.signOut().getOrThrow()
    }
}
