package com.lighthouse.domain.usecase.user

import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return authRepository.isLogin()
    }
}
