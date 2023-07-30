package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.Security
import com.lighthouse.beep.model.user.Subscription
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {

    val userConfig: Flow<UserConfig>

    suspend fun setAuthInfo(authInfo: AuthInfo)

    suspend fun setSubscription(subscription: Subscription)

    suspend fun setSecurity(security: Security)

    suspend fun setThemeOption(themeOption: ThemeOption)

    suspend fun clear()
}
