package com.lighthouse.beep.domain.usecase.auth

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.model.deviceconfig.AuthInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Flow<AuthInfo> = authRepository.authInfo
}
