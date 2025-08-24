package com.lighthouse.beep.domain.usecase.auth

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import javax.inject.Inject

class GetCurrentUserUidUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    
    operator fun invoke(): String = authRepository.currentAuthInfo?.userUid ?: ""
}