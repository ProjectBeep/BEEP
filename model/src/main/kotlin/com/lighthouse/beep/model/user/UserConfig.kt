package com.lighthouse.beep.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserConfig(
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
) {
    companion object {
        val Default = UserConfig()
    }
}

enum class ThemeOption {
    @SerialName("system")
    SYSTEM,

    @SerialName("light")
    LIGHT,

    @SerialName("dark")
    DARK,
}
