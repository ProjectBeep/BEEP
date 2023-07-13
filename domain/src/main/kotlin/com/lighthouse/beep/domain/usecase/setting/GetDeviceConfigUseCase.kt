package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDeviceConfigUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<DeviceConfig> {
        return userRepository.deviceConfig
    }
}
