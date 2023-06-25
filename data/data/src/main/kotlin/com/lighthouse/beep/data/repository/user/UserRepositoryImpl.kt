package com.lighthouse.beep.data.repository.user

import com.lighthouse.beep.model.user.SecurityOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    private val localUserAuthDataSource: LocalUserAuthDataSource,
) : UserRepository {

    override suspend fun setPinPassword(userId: String, newPin: String): Result<Unit> =
        localUserAuthDataSource.setPinPassword(userId, newPin)

    override fun confirmPinPassword(userId: String, pin: String): Result<Boolean> {
        return localUserAuthDataSource.confirmPinPassword(userId, pin)
    }

    override suspend fun setSecurityOption(
        userId: String,
        securityOption: SecurityOption,
    ): Result<Unit> {
        return localUserDataSource.setSecurityOption(userId, securityOption)
    }

    override fun getSecurityOption(userId: String): Flow<Result<SecurityOption>> {
        return localUserDataSource.getSecurityOption(userId)
    }

    override suspend fun setNotificationEnable(userId: String, enable: Boolean): Result<Unit> {
        return localUserDataSource.setNotificationEnable(userId, enable)
    }

    override fun getNotificationEnable(userId: String): Flow<Result<Boolean>> {
        return localUserDataSource.getNotificationEnable(userId)
    }

    override suspend fun setFilterExpired(userId: String, enable: Boolean): Result<Unit> {
        return localUserDataSource.setFilterExpired(userId, enable)
    }

    override fun getFilterExpired(userId: String): Flow<Result<Boolean>> {
        return localUserDataSource.getFilterExpired(userId)
    }

    override suspend fun transferData(userId: String, newUserId: String): Result<Unit> =
        runCatching {
            localUserDataSource.transferData(userId, newUserId).getOrThrow()
            localUserAuthDataSource.transferData(userId, newUserId).getOrThrow()
        }

    override suspend fun withdrawal(userId: String): Result<Unit> {
        return runCatching {
            localUserDataSource.withdrawal(userId).getOrThrow()
            localUserAuthDataSource.clearData(userId)
        }
    }
}
