package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.deviceconfig.AuthInfo
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.Security
import com.lighthouse.beep.model.deviceconfig.Subscription
import com.lighthouse.beep.model.deviceconfig.ThemeOption
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val deviceConfig: Flow<DeviceConfig>

    suspend fun getAuthInfo(): AuthInfo

    suspend fun setAuthInfo(authInfo: AuthInfo)

    suspend fun getHash(): RecentHash

    suspend fun setHash(hash: RecentHash)

    suspend fun getSubscription(): Subscription

    suspend fun setSubscription(subscription: Subscription)

    suspend fun getSecurity(): Security

    suspend fun setSecurity(security: Security)

    suspend fun getThemeOption(): ThemeOption

    suspend fun setThemeOption(themeOption: ThemeOption)

    suspend fun logout()
}
