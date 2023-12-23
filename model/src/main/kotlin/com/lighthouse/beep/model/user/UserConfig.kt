package com.lighthouse.beep.model.user

import android.net.Uri
import com.lighthouse.beep.model.serializer.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserConfig(
    val authInfo: AuthInfo = AuthInfo.Default,
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
) {
    companion object {
        val Default = UserConfig()
    }
}

@Serializable
data class AuthInfo(
    val userUid: String = "",
    val provider: AuthProvider = AuthProvider.NONE,
    val displayName: String = "",
    val email: String = "",
    @Serializable(with = UriSerializer::class)
    val photoUrl: Uri? = Uri.EMPTY,
) {
    companion object {
        val Default = AuthInfo()
    }
}

enum class AuthProvider(
    val firebaseName: String,
) {
    @SerialName("NONE")
    NONE(firebaseName = "NONE"),

    @SerialName("NAVER")
    NAVER(firebaseName = "NAVER"),

    @SerialName("KAKAO")
    KAKAO(firebaseName = "KAKAO"),

    @SerialName("GOOGLE")
    GOOGLE(firebaseName = "GOOGLE"),

    @SerialName("GUEST")
    GUEST(firebaseName = "GUEST");

    companion object {
        fun of(firebaseName: String): AuthProvider {
            return runCatching {
                entries.find { it.firebaseName == firebaseName } ?: NONE
            }.getOrDefault(NONE)
        }
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
