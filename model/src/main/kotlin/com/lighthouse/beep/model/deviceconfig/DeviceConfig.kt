package com.lighthouse.beep.model.deviceconfig

import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfig(
    val beepGuide: BeepGuide = BeepGuide.Default,
) {
    companion object {
        val Default = DeviceConfig()
    }
}

@Serializable
data class BeepGuide(
    val permission: Boolean = false,
    val cropImagePen: Boolean = false,
) {
    companion object {
        val Default = BeepGuide()
    }
}