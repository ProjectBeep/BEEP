package com.lighthouse.repository.user

import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.libs.ciphertool.EncryptData
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    suspend fun setEncryptData(userId: String, encryptData: EncryptData): Result<Unit>

    fun getEncryptData(userId: String): Flow<Result<EncryptData>>

    suspend fun setSecurityOption(
        userId: String,
        securityOption: SecurityOption
    ): Result<Unit>

    fun getSecurityOption(userId: String): Flow<Result<SecurityOption>>

    suspend fun setNotificationEnable(
        userId: String,
        enable: Boolean
    ): Result<Unit>

    fun getNotificationEnable(userId: String): Flow<Result<Boolean>>

    suspend fun setFilterExpired(
        userId: String,
        filterExpired: Boolean
    ): Result<Unit>

    fun getFilterExpired(userId: String): Flow<Result<Boolean>>

    suspend fun transferData(
        oldUserId: String,
        newUserId: String
    ): Result<Unit>

    suspend fun withdrawal(userId: String): Result<Unit>
}
