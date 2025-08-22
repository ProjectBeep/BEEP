package com.lighthouse.beep.model.user

import android.net.Uri
import androidx.annotation.DrawableRes
import com.lighthouse.beep.model.R

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
    @DrawableRes val iconResId: Int = 0,
) {
    NONE(
        firebaseName = "NONE",
    ),

    NAVER(
        firebaseName = "NAVER",
        iconResId = R.drawable.icon_auth_provider_naver,
    ),

    KAKAO(
        firebaseName = "KAKAO",
        iconResId = R.drawable.icon_auth_provider_kakao,
    ),

    GOOGLE(
        firebaseName = "GOOGLE",
        iconResId = R.drawable.icon_auth_provider_google,
    ),

    GUEST(firebaseName = "GUEST");

    val isAvailableLogout
        get() = this != NONE && this != GUEST

    companion object {
        fun of(firebaseName: String): AuthProvider {
            return runCatching {
                entries.find { it.firebaseName == firebaseName } ?: NONE
            }.getOrDefault(NONE)
        }
    }
}
