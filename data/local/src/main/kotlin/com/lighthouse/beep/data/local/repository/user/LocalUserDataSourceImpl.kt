package com.lighthouse.beep.data.local.repository.user

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.UserConfigSerializer
import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.Security
import com.lighthouse.beep.model.user.Subscription
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import javax.inject.Inject

internal class LocalUserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserConfig>,
) : LocalUserDataSource {

    override val userConfig = dataStore.data

    override suspend fun setAuthInfo(authInfo: AuthInfo) {
        dataStore.updateData {
            it.copy(authInfo = authInfo)
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
            UserConfigSerializer.defaultValue
        }
    }
}
