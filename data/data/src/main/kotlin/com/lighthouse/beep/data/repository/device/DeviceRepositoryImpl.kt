package com.lighthouse.beep.data.repository.device

import com.lighthouse.beep.model.deviceconfig.BeepGuide
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DeviceRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDeviceDataSource,
) : DeviceRepository {

    override val deviceConfig: Flow<DeviceConfig>
        get() = localDataSource.deviceConfig

    override suspend fun setBeepGuide(beepGuide: BeepGuide) {
        localDataSource.setBeepGuide(beepGuide)
    }
}
