package com.lighthouse.domain.repository.user

import com.lighthouse.beep.model.auth.EncryptData
import com.lighthouse.beep.model.user.SecurityOption
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun isLogin(): Flow<Boolean>
    suspend fun login(userId: String): Result<Unit>
    suspend fun signOut(): Result<Unit>

    suspend fun setPinPassword(userId: String, encryptData: EncryptData): Result<Unit>
    fun getPinPassword(userId: String): Flow<Result<EncryptData>>

    suspend fun setSecurityOption(userId: String, securityOption: SecurityOption): Result<Unit>
    fun getSecurityOption(userId: String): Flow<Result<SecurityOption>>

    suspend fun setNotificationEnable(userId: String, enable: Boolean): Result<Unit>
    fun getNotificationEnable(userId: String): Flow<Result<Boolean>>

    suspend fun setFilterExpired(userId: String, enable: Boolean): Result<Unit>
    fun getFilterExpired(userId: String): Flow<Result<Boolean>>

    suspend fun transferData(userId: String, newUserId: String): Result<Unit>
    suspend fun withdrawal(userId: String): Result<Unit>
}
