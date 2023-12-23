package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalUserDataSource,
) : UserRepository {

    override val userConfig: Flow<UserConfig> = localDataSource.userConfig

    override suspend fun setAuthInfo(transform: (AuthInfo) -> AuthInfo) {
        localDataSource.setAuthInfo(transform)
    }

    override suspend fun setThemeOption(newValue: ThemeOption) {
        localDataSource.setThemeOption(newValue)
    }

    override suspend fun logout() {
        localDataSource.clear()
    }
}
