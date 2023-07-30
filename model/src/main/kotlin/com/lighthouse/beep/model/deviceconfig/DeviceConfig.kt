package com.lighthouse.beep.model.deviceconfig

import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfig(
    val hash: RecentHash = RecentHash(
        backup = "",
    ),
    val shownGuidePage: ShownGuidePage = ShownGuidePage(
        permission = false,
    ),
)

@Serializable
data class RecentHash(
    val backup: String,
)

@Serializable
data class ShownGuidePage(
    val permission: Boolean,
)
