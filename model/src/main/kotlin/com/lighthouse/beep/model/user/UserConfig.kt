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
    @SerialName("none")
    NONE(firebaseName = "none"),

    @SerialName("naver")
    NAVER(firebaseName = "naver"),

    @SerialName("kakao")
    KAKAO(firebaseName = "kakao"),

    @SerialName("google")
    GOOGLE(firebaseName = "google"),

    @SerialName("guest")
    GUEST(firebaseName = "anonymous");

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
