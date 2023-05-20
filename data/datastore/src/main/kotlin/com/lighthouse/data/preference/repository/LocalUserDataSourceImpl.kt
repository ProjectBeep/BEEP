package com.lighthouse.data.preference.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import com.lighthouse.beep.model.exception.common.NotFoundException
import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.data.preference.ext.booleanKey
import com.lighthouse.data.preference.ext.byteArrayKey
import com.lighthouse.data.preference.ext.stringKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalUserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalUserDataSource {

    override suspend fun setSecurityOption(
        userId: String,
        securityOption: SecurityOption,
    ): Result<Unit> = runCatching {
        val key = stringKey(userId, KEY_NAME_SECURITY_OPTION)
        dataStore.edit { pref ->
            pref[key] = securityOption.name
        }
    }

    override fun getSecurityOption(
        userId: String,
    ): Flow<Result<SecurityOption>> {
        val key = stringKey(userId, KEY_NAME_SECURITY_OPTION)
        return dataStore.data.map { pref ->
            runCatching {
                val value = pref[key] ?: throw NotFoundException("Security 값이 없습니다.")
                SecurityOption.valueOf(value)
            }
        }.distinctUntilChanged()
    }

    override suspend fun setNotificationEnable(
        userId: String,
        enable: Boolean,
    ): Result<Unit> = runCatching {
        return setBoolean(userId, KEY_NAME_NOTIFICATION_ENABLE, enable)
    }

    override fun getNotificationEnable(
        userId: String,
    ): Flow<Result<Boolean>> {
        return getBoolean(userId, KEY_NAME_NOTIFICATION_ENABLE).distinctUntilChanged()
    }

    override suspend fun setFilterExpired(
        userId: String,
        filterExpired: Boolean,
    ): Result<Unit> {
        return setBoolean(userId, KEY_NAME_FILTER_EXPIRED, filterExpired)
    }

    override fun getFilterExpired(userId: String): Flow<Result<Boolean>> {
        return getBoolean(userId, KEY_NAME_FILTER_EXPIRED).distinctUntilChanged()
    }

    override suspend fun transferData(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit> = runCatching {
        val data = dataStore.data.first()
        val securityOption = data[stringKey(oldUserId, KEY_NAME_SECURITY_OPTION)]?.let {
            SecurityOption.valueOf(it)
        } ?: throw NotFoundException("Security 값이 없습니다.")
        val notificationEnable = data[booleanKey(oldUserId, KEY_NAME_NOTIFICATION_ENABLE)]
        val filterExpired = data[booleanKey(oldUserId, KEY_NAME_FILTER_EXPIRED)]

        dataStore.edit { pref ->
            pref[stringKey(newUserId, KEY_NAME_SECURITY_OPTION)] = securityOption.name
            pref[booleanKey(newUserId, KEY_NAME_NOTIFICATION_ENABLE)] = notificationEnable ?: false
            pref[booleanKey(newUserId, KEY_NAME_FILTER_EXPIRED)] = filterExpired ?: false
        }

        clearData(oldUserId)
    }

    override suspend fun withdrawal(userId: String): Result<Unit> = runCatching {
        clearData(userId)
    }

    private suspend fun clearData(userId: String) {
        dataStore.edit { pref ->
            pref.remove(byteArrayKey(userId, KEY_NAME_SECURITY_OPTION))
            pref.remove(byteArrayKey(userId, KEY_NAME_NOTIFICATION_ENABLE))
            pref.remove(byteArrayKey(userId, KEY_NAME_FILTER_EXPIRED))
        }
    }

    private suspend fun setBoolean(
        userId: String,
        keyName: String,
        value: Boolean,
    ): Result<Unit> = runCatching {
        val key = booleanKey(userId, keyName)
        dataStore.edit { pref ->
            pref[key] = value
        }
    }

    private fun getBoolean(userId: String, keyName: String): Flow<Result<Boolean>> {
        val key = booleanKey(userId, keyName)
        return dataStore.data.map { pref ->
            runCatching {
                pref[key] ?: throw NotFoundException("$keyName 값이 없습니다.")
            }
        }
    }

    companion object {
        private const val KEY_NAME_SECURITY_OPTION = "SecurityOption"
        private const val KEY_NAME_NOTIFICATION_ENABLE = "NotificationEnable"
        private const val KEY_NAME_FILTER_EXPIRED = "FilterExpired"
    }
}
