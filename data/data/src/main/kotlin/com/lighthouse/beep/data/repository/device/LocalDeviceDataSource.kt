package com.lighthouse.beep.data.repository.device

import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.ShownGuidePage
import kotlinx.coroutines.flow.Flow

interface LocalDeviceDataSource {

    val deviceConfig: Flow<DeviceConfig>

    suspend fun setHash(hash: RecentHash)

    suspend fun setShownGuidePage(shownGuidePage: ShownGuidePage)

    suspend fun clear()
}
