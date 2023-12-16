package com.lighthouse.beep.data.local.repository.device

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.DeviceConfigSerializer
import com.lighthouse.beep.data.repository.device.LocalDeviceDataSource
import com.lighthouse.beep.model.deviceconfig.BeepGuide
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import javax.inject.Inject

internal class LocalDeviceDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<DeviceConfig>,
) : LocalDeviceDataSource {

    override val deviceConfig = dataStore.data

    override suspend fun setBeepGuide(beepGuide: BeepGuide) {
        dataStore.updateData {
            it.copy(beepGuide = beepGuide)
        }
    }

    override suspend fun clear() {
        dataStore.updateData {
            DeviceConfigSerializer.defaultValue
        }
    }
}
