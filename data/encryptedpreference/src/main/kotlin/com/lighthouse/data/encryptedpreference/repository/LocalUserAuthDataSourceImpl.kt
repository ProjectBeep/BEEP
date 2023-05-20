package com.lighthouse.data.encryptedpreference.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.lighthouse.beep.data.repository.user.LocalUserAuthDataSource
import javax.inject.Inject

internal class LocalUserAuthDataSourceImpl @Inject constructor(
    private val encryptedPreferences: SharedPreferences,
) : LocalUserAuthDataSource {

    @Suppress("SameParameterValue")
    private fun createKey(userId: String, key: String): String {
        return "${userId}_$key"
    }

    override fun setPinPassword(userId: String, pin: String): Result<Unit> = runCatching {
        val key = createKey(userId, KEY_PIN_PASSWORD)
        encryptedPreferences.edit {
            putString(key, pin)
        }
    }

    override fun confirmPinPassword(userId: String, pin: String): Result<Boolean> {
        val key = createKey(userId, KEY_PIN_PASSWORD)
        return runCatching {
            val password = encryptedPreferences.getString(key, "")
            pin == password
        }
    }

    override fun transferData(userId: String, newUserId: String): Result<Unit> = runCatching {
        val pinPasswordKey = createKey(userId, KEY_PIN_PASSWORD)
        val pinPassword = encryptedPreferences.getString(pinPasswordKey, "") ?: ""

        encryptedPreferences.edit {
            putString(createKey(newUserId, KEY_PIN_PASSWORD), pinPassword)
            remove(pinPasswordKey)
        }
    }

    override fun clearData(userId: String): Result<Unit> = runCatching {
        encryptedPreferences.edit {
            remove(createKey(userId, KEY_PIN_PASSWORD))
        }
    }

    companion object {
        private const val KEY_PIN_PASSWORD = "PinPassword"
    }
}
