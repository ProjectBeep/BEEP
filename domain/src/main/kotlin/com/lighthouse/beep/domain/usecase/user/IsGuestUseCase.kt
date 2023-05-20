package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Flow<Boolean> {
        return authRepository.isGuest()
    }
}
