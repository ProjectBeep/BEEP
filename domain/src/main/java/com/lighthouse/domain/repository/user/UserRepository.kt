package com.lighthouse.domain.repository.user

import com.lighthouse.beep.model.user.SecurityOption
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun setPinPassword(userId: String, newPin: String): Result<Unit>
    fun confirmPinPassword(userId: String, pin: String): Flow<Result<Boolean>>

    suspend fun setSecurityOption(userId: String, securityOption: SecurityOption): Result<Unit>
    fun getSecurityOption(userId: String): Flow<Result<SecurityOption>>

    suspend fun setNotificationEnable(userId: String, enable: Boolean): Result<Unit>
    fun getNotificationEnable(userId: String): Flow<Result<Boolean>>

    suspend fun setFilterExpired(userId: String, enable: Boolean): Result<Unit>
    fun getFilterExpired(userId: String): Flow<Result<Boolean>>

    suspend fun transferData(userId: String, newUserId: String): Result<Unit>
    suspend fun withdrawal(userId: String): Result<Unit>
}
