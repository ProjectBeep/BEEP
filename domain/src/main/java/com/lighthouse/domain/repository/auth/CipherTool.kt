package com.lighthouse.domain.repository.auth

import com.lighthouse.beep.model.auth.EncryptData

interface CipherTool {

    fun encrypt(alias: String, data: String): Result<EncryptData>

    fun decrypt(alias: String, data: EncryptData): Result<String>
}
