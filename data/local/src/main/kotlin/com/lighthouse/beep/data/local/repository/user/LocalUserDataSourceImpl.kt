package com.lighthouse.beep.data.local.repository.user

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.DeviceConfigSerializer
import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import com.lighthouse.beep.model.deviceconfig.AuthInfo
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.Security
import com.lighthouse.beep.model.deviceconfig.Subscription
import com.lighthouse.beep.model.deviceconfig.ThemeOption
import javax.inject.Inject

internal class LocalUserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<DeviceConfig>,
) : LocalUserDataSource {

    override val deviceConfig = dataStore.data

    override suspend fun setAuthInfo(authInfo: AuthInfo) {
        dataStore.updateData {
            it.copy(authInfo = authInfo)
        }
    }

    override suspend fun setHash(hash: RecentHash) {
        dataStore.updateData {
            it.copy(hash = hash)
        }
    }

    override suspend fun setSubscription(subscription: Subscription) {
        dataStore.updateData {
            it.copy(subscription = subscription)
        }
    }

    override suspend fun setSecurity(security: Security) {
        dataStore.updateData {
            it.copy(security = security)
        }
    }

    override suspend fun setThemeOption(themeOption: ThemeOption) {
        dataStore.updateData {
            it.copy(themeOption = themeOption)
        }
    }

    override suspend fun clear() {
        dataStore.updateData {
            DeviceConfigSerializer.defaultValue
        }
    }
}
