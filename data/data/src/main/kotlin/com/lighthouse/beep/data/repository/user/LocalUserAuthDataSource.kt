package com.lighthouse.beep.data.repository.user

interface LocalUserAuthDataSource {

    fun setPinPassword(userId: String, pin: String): Result<Unit>

    fun confirmPinPassword(userId: String, pin: String): Result<Boolean>

    fun clearData(userId: String): Result<Unit>

    fun transferData(userId: String, newUserId: String): Result<Unit>
}
