package com.lighthouse.beep.data.repository.device

import com.lighthouse.beep.model.deviceconfig.BeepGuide
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    val deviceConfig: Flow<DeviceConfig>

    suspend fun setBeepGuide(transform: (BeepGuide) -> BeepGuide)
}
