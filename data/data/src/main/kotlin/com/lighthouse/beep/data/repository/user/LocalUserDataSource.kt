package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.deviceconfig.AuthInfo
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.deviceconfig.RecentHash
import com.lighthouse.beep.model.deviceconfig.Security
import com.lighthouse.beep.model.deviceconfig.ShownGuidePage
import com.lighthouse.beep.model.deviceconfig.Subscription
import com.lighthouse.beep.model.deviceconfig.ThemeOption
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {

    val deviceConfig: Flow<DeviceConfig>

    suspend fun setAuthInfo(authInfo: AuthInfo)

    suspend fun setHash(hash: RecentHash)

    suspend fun setShownGuidePage(shownGuidePage: ShownGuidePage)

    suspend fun setSubscription(subscription: Subscription)

    suspend fun setSecurity(security: Security)

    suspend fun setThemeOption(themeOption: ThemeOption)

    suspend fun clear()
}
