package com.lighthouse.beep.data.local.repository.device

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.DeviceConfigSerializer
import com.lighthouse.beep.data.repository.device.LocalDeviceDataSource
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.ShownGuidePage
import javax.inject.Inject

internal class LocalDeviceDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<DeviceConfig>,
) : LocalDeviceDataSource {

    override val deviceConfig = dataStore.data

    override suspend fun setHash(hash: RecentHash) {
        dataStore.updateData {
            it.copy(hash = hash)
        }
    }

    override suspend fun setShownGuidePage(shownGuidePage: ShownGuidePage) {
        dataStore.updateData {
            it.copy(shownGuidePage = shownGuidePage)
        }
    }

    override suspend fun clear() {
        dataStore.updateData {
            DeviceConfigSerializer.defaultValue
        }
    }
}
