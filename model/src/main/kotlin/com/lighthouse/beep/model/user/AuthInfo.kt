package com.lighthouse.beep.model.user

import android.net.Uri

data class AuthInfo(
    val userUid: String = "",
    val provider: AuthProvider = AuthProvider.NONE,
    val displayName: String = "",
    val email: String = "",
    val photoUrl: Uri? = Uri.EMPTY,
) {
    companion object {
        val Default = AuthInfo()
    }
}

enum class AuthProvider(
    val firebaseName: String,
) {
    NONE(firebaseName = "NONE"),

    NAVER(firebaseName = "NAVER"),

    KAKAO(firebaseName = "KAKAO"),

    GOOGLE(firebaseName = "GOOGLE"),

    GUEST(firebaseName = "GUEST");

    companion object {
        fun of(firebaseName: String): AuthProvider {
            return runCatching {
                entries.find { it.firebaseName == firebaseName } ?: NONE
            }.getOrDefault(NONE)
        }
    }
}
