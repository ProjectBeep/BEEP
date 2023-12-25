package com.lighthouse.beep.data.local.repository.user

import androidx.datastore.core.DataStore
import com.lighthouse.beep.data.local.serializer.UserConfigSerializer
import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class LocalUserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserConfig>,
) : LocalUserDataSource {

    override val userConfig = dataStore.data

    override suspend fun setThemeOption(newValue: ThemeOption) {
        val oldValue = userConfig.firstOrNull()?.themeOption ?: ThemeOption.SYSTEM
        if (oldValue != newValue) {
            dataStore.updateData {
                it.copy(themeOption = newValue)
            }
        }
    }

    override suspend fun clear() {
        dataStore.updateData {
            UserConfigSerializer.defaultValue
        }
    }
}
