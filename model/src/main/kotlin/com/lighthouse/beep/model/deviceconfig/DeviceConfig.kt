package com.lighthouse.beep.model.deviceconfig

import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfig(
    val beepGuide: BeepGuide = BeepGuide(
        permission = false,
        cropImagePen = false,
    ),
)

@Serializable
data class BeepGuide(
    val permission: Boolean,
    val cropImagePen: Boolean,
)