package com.lighthouse.auth.repository

import com.lighthouse.beep.model.auth.EncryptData

interface CipherTool {

    fun encrypt(alias: String, data: String): Result<EncryptData>

    fun decrypt(alias: String, data: EncryptData): Result<String>
}
