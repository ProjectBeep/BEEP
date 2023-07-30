package com.lighthouse.beep.data.repository.device

import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.ShownGuidePage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class DeviceRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDeviceDataSource,
) : DeviceRepository {

    override val deviceConfig: Flow<DeviceConfig>
        get() = localDataSource.deviceConfig

    override suspend fun getHash(): RecentHash {
        return deviceConfig.first().hash
    }

    override suspend fun setHash(hash: RecentHash) {
        localDataSource.setHash(hash)
    }

    override suspend fun getShownGuidePage(): ShownGuidePage {
        return deviceConfig.first().shownGuidePage
    }

    override suspend fun setShownGuidePage(shownGuidePage: ShownGuidePage) {
        localDataSource.setShownGuidePage(shownGuidePage)
    }
}
