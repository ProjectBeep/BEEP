package com.lighthouse.beep.data.local.serializer

import androidx.datastore.core.Serializer
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
internal object UserConfigSerializer : Serializer<UserConfig> {
    override val defaultValue: UserConfig
        get() = UserConfig.Default

    override suspend fun readFrom(input: InputStream): UserConfig {
        return runCatching {
            Json.decodeFromString(
                deserializer = UserConfig.serializer(),
                string = input.readBytes().decodeToString(),
            )
        }.getOrDefault(defaultValue)
    }

    override suspend fun writeTo(t: UserConfig, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = UserConfig.serializer(),
                value = t,
            ).encodeToByteArray(),
        )
    }
}
