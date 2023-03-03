package com.lighthouse.repository.user

import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.domain.repository.user.UserRepository
import com.lighthouse.libs.ciphertool.CipherTool
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.map

internal class UserRepositoryImpl @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : UserRepository {

    override suspend fun setPinPassword(userId: String, newPin: String): Result<Unit> =
        runCatching {
            val encryptData = CipherTool.encrypt(SecurityOption.PIN.name, newPin)
            userPreferenceRepository.setEncryptData(userId, encryptData).getOrThrow()
        }

    override fun confirmPinPassword(userId: String, pin: String): Flow<Result<Boolean>> {
        return userPreferenceRepository.getEncryptData(userId).map {
            runCatching {
                val encryptPin = it.getOrThrow()
                val decryptData = CipherTool.decrypt(SecurityOption.PIN.name, encryptPin)
                decryptData == pin
            }
        }
    }

    override suspend fun setSecurityOption(
        userId: String,
        securityOption: SecurityOption
    ): Result<Unit> {
        return userPreferenceRepository.setSecurityOption(userId, securityOption)
    }

    override fun getSecurityOption(userId: String): Flow<Result<SecurityOption>> {
        return userPreferenceRepository.getSecurityOption(userId)
    }

    override suspend fun setNotificationEnable(userId: String, enable: Boolean): Result<Unit> {
        return userPreferenceRepository.setNotificationEnable(userId, enable)
    }

    override fun getNotificationEnable(userId: String): Flow<Result<Boolean>> {
        return userPreferenceRepository.getNotificationEnable(userId)
    }

    override suspend fun setFilterExpired(userId: String, enable: Boolean): Result<Unit> {
        return userPreferenceRepository.setFilterExpired(userId, enable)
    }

    override fun getFilterExpired(userId: String): Flow<Result<Boolean>> {
        return userPreferenceRepository.getFilterExpired(userId)
    }

    override suspend fun transferData(userId: String, newUserId: String): Result<Unit> {
        return userPreferenceRepository.transferData(userId, newUserId)
    }

    override suspend fun withdrawal(userId: String): Result<Unit> {
        return userPreferenceRepository.withdrawal(userId)
    }
}
