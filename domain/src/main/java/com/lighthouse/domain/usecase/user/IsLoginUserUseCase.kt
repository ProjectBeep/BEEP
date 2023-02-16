package com.lighthouse.domain.usecase.user

import com.lighthouse.domain.repository.user.UserRepository
import javax.inject.Inject

class IsLoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Boolean {
        return userRepository.isLogin()
    }
}
