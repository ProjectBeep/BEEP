package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.deviceconfig.AuthInfo
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.Security
import com.lighthouse.beep.model.deviceconfig.Subscription
import com.lighthouse.beep.model.deviceconfig.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalUserDataSource,
) : UserRepository {

    override val deviceConfig: Flow<DeviceConfig> = localDataSource.deviceConfig

    override suspend fun getAuthInfo(): AuthInfo {
        return deviceConfig.first().authInfo
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo) {
        localDataSource.setAuthInfo(authInfo)
    }

    override suspend fun getHash(): RecentHash {
        return deviceConfig.first().hash
    }

    override suspend fun setHash(hash: RecentHash) {
        localDataSource.setHash(hash)
    }

    override suspend fun getSubscription(): Subscription {
        return deviceConfig.first().subscription
    }

    override suspend fun setSubscription(subscription: Subscription) {
        localDataSource.setSubscription(subscription)
    }

    override suspend fun getSecurity(): Security {
        return deviceConfig.first().security
    }

    override suspend fun setSecurity(security: Security) {
        localDataSource.setSecurity(security)
    }

    override suspend fun getThemeOption(): ThemeOption {
        return deviceConfig.first().themeOption
    }

    override suspend fun setThemeOption(themeOption: ThemeOption) {
        localDataSource.setThemeOption(themeOption)
    }

    override suspend fun logout() {
        localDataSource.clear()
    }
}
