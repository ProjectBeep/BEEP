package com.lighthouse.beep.data.local.repository.device

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.DeviceConfigSerializer
import com.lighthouse.beep.data.repository.device.LocalDeviceDataSource
import com.lighthouse.beep.model.deviceconfig.BeepGuide
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class LocalDeviceDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<DeviceConfig>,
) : LocalDeviceDataSource {

    override val deviceConfig = dataStore.data

    override suspend fun setBeepGuide(transform: (BeepGuide) -> BeepGuide) {
        val oldValue = deviceConfig.firstOrNull()?.beepGuide ?: BeepGuide.Default
        val newValue = transform(oldValue)
        if (oldValue != newValue) {
            dataStore.updateData {
                it.copy(beepGuide = newValue)
            }
        }
    }

    override suspend fun clear() {
        dataStore.updateData {
            DeviceConfigSerializer.defaultValue
        }
    }
}
