package com.lighthouse.auth.repository

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.lighthouse.beep.model.auth.EncryptData
import com.lighthouse.domain.repository.auth.CipherTool
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

internal class CipherToolImpl @Inject constructor() : CipherTool {

    private val keyStore by lazy {
        KeyStore.getInstance(KEYSTORE_NAME).apply {
            load(null)
        }
    }

    override fun encrypt(alias: String, data: String): Result<EncryptData> = runCatching {
        val secretKey = getSecretKey(alias)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        EncryptData(
            cipher.doFinal(data.toByteArray()),
            cipher.iv
        )
    }

    override fun decrypt(alias: String, data: EncryptData): Result<String> = runCatching {
        val secretKey = getSecretKey(alias)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(data.iv))
        String(cipher.doFinal(data.data))
    }

    private fun getSecretKey(alias: String): Key {
        return if (keyStore.isKeyEntry(alias).not()) {
            generateKey(alias)
        } else {
            keyStore.getKey(alias, null)
        }
    }

    private fun generateKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(BLOCK_MODE)
            .setEncryptionPaddings(ENCRYPTION_PADDINGS)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(false)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    companion object {
        private const val KEYSTORE_NAME = "AndroidKeyStore"

        private const val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val ENCRYPTION_PADDINGS = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$ENCRYPTION_PADDINGS"
    }
}
