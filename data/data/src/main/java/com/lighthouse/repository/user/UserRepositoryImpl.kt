package com.lighthouse.repository.user

import com.lighthouse.beep.model.auth.EncryptData
import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : UserRepository {

    override fun isLogin(): Flow<Boolean> {
        return userPreferenceRepository.getLoginUserUid().map {
            it.getOrDefault("") != ""
        }
    }

    override suspend fun login(userId: String): Result<Unit> {
        return userPreferenceRepository.setLoginUserUid(userId)
    }

//    override fun getUserId(): String {
//        return authRepository.getCurrentUserId()
//    }
//
//    override fun isGuest(): Flow<Boolean> {
//        return authRepository.isGuest()
//    }

//    override suspend fun setPinPassword(userId: String, pinPassword: String): Result<Unit> = runCatching {
//        val userId = authRepository.getCurrentUserId()
//        val encryptData = cipherTool.encrypt(SecurityOption.PIN.name, pinPassword).getOrThrow()
//        userPreferenceRepository.setEncryptData(userId, encryptData).getOrThrow()
//    }
//
//    override suspend fun confirmPinPassword(userId: String, pinPassword: String): Result<Boolean> = runCatching {
//        val encryptData = userPreferenceRepository.getEncryptData(userId).getOrThrow()
//        val decryptData = cipherTool.decrypt(SecurityOption.PIN.name, encryptData).getOrThrow()
//        pinPassword == decryptData
//    }

    override suspend fun setPinPassword(userId: String, encryptData: EncryptData): Result<Unit> {
        return userPreferenceRepository.setEncryptData(userId, encryptData)
    }

    override fun getPinPassword(userId: String): Flow<Result<EncryptData>> {
        return userPreferenceRepository.getEncryptData(userId)
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

    override suspend fun transferData(oldUserId: String, newUserId: String): Result<Unit> {
        return userPreferenceRepository.transferData(oldUserId, newUserId)
    }

    override suspend fun clearData(userId: String): Result<Unit> {
        return userPreferenceRepository.clearData(userId)
    }
}
