package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userConfig: Flow<UserConfig>

    suspend fun setThemeOption(newValue: ThemeOption)

    suspend fun logout()
}
