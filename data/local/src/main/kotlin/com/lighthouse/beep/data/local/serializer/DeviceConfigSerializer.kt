package com.lighthouse.beep.data.local.serializer

import androidx.datastore.core.Serializer
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
internal object DeviceConfigSerializer : Serializer<DeviceConfig> {
    override val defaultValue: DeviceConfig
        get() = DeviceConfig.Default

    override suspend fun readFrom(input: InputStream): DeviceConfig {
        return runCatching {
            Json.decodeFromString(
                deserializer = DeviceConfig.serializer(),
                string = input.readBytes().decodeToString(),
            )
        }.getOrDefault(defaultValue)
    }

    override suspend fun writeTo(t: DeviceConfig, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = DeviceConfig.serializer(),
                value = t,
            ).encodeToByteArray(),
        )
    }
}
