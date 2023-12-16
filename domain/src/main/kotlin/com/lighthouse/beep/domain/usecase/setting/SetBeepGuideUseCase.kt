package com.lighthouse.beep.domain.usecase.setting

import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.model.deviceconfig.BeepGuide
import javax.inject.Inject

class SetBeepGuideUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
){

    suspend operator fun invoke(beepGuide: BeepGuide) {
        deviceRepository.setBeepGuide(beepGuide)
    }
}