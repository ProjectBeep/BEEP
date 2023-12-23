package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userConfig: Flow<UserConfig>

    suspend fun setAuthInfo(transform: (AuthInfo) -> AuthInfo)

    suspend fun setThemeOption(newValue: ThemeOption)

    suspend fun logout()
}
