package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.Security
import com.lighthouse.beep.model.user.Subscription
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalUserDataSource,
) : UserRepository {

    override val userConfig: Flow<UserConfig> = localDataSource.userConfig

    override suspend fun getAuthInfo(): AuthInfo {
        return userConfig.first().authInfo
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo) {
        localDataSource.setAuthInfo(authInfo)
    }

    override suspend fun getSubscription(): Subscription {
        return userConfig.first().subscription
    }

    override suspend fun setSubscription(subscription: Subscription) {
        localDataSource.setSubscription(subscription)
    }

    override suspend fun getSecurity(): Security {
        return userConfig.first().security
    }

    override suspend fun setSecurity(security: Security) {
        localDataSource.setSecurity(security)
    }

    override suspend fun getThemeOption(): ThemeOption {
        return userConfig.first().themeOption
    }

    override suspend fun setThemeOption(themeOption: ThemeOption) {
        localDataSource.setThemeOption(themeOption)
    }

    override suspend fun logout() {
        localDataSource.clear()
    }
}
