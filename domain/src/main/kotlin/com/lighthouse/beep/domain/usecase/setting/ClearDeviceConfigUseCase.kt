package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.data.repository.user.UserRepository
import javax.inject.Inject

class ClearDeviceConfigUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke() {
        userRepository.logout()
    }
}
