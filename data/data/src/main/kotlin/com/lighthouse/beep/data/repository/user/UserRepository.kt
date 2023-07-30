package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.Security
import com.lighthouse.beep.model.user.Subscription
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userConfig: Flow<UserConfig>

    suspend fun getAuthInfo(): AuthInfo

    suspend fun setAuthInfo(authInfo: AuthInfo)

    suspend fun getSubscription(): Subscription

    suspend fun setSubscription(subscription: Subscription)

    suspend fun getSecurity(): Security

    suspend fun setSecurity(security: Security)

    suspend fun getThemeOption(): ThemeOption

    suspend fun setThemeOption(themeOption: ThemeOption)

    suspend fun logout()
}
