package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.user.AuthInfo
import javax.inject.Inject

class UpdateAuthInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(authInfo: AuthInfo) {
        userRepository.setAuthInfo(authInfo)
    }
}
