package com.lighthouse.beep.domain.usecase.auth

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import javax.inject.Inject

class WithdrawalAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    
    suspend operator fun invoke() {
        authRepository.withdrawalAccount()
    }
}