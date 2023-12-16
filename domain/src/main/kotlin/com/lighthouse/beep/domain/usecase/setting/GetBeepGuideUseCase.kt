package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.model.deviceconfig.BeepGuide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBeepGuideUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
){

    operator fun invoke(): Flow<BeepGuide> {
        return deviceRepository.deviceConfig.map {
            it.beepGuide
        }
    }
}