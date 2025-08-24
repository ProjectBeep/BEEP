package com.lighthouse.beep.domain.usecase.auth

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.model.user.AuthInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentAuthInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    
    operator fun invoke(): Flow<AuthInfo?> = authRepository.authInfoFlow
}