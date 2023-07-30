package com.lighthouse.beep.domain.usecase.setting.guide

import com.lighthouse.beep.data.repository.device.DeviceRepository
import javax.inject.Inject

class SetShownGuidePermission @Inject constructor(
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(shownGuidePermission: Boolean) {
        val currentShownGuidePage = deviceRepository.getShownGuidePage()
        deviceRepository.setShownGuidePage(
            currentShownGuidePage.copy(permission = shownGuidePermission),
        )
    }
}
