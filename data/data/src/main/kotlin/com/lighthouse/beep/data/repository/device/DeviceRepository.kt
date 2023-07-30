package com.lighthouse.beep.data.repository.device

import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.ShownGuidePage
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    val deviceConfig: Flow<DeviceConfig>

    suspend fun getHash(): RecentHash

    suspend fun setHash(hash: RecentHash)

    suspend fun getShownGuidePage(): ShownGuidePage

    suspend fun setShownGuidePage(shownGuidePage: ShownGuidePage)
}
