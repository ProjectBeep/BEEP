package com.lighthouse.beep.auth.model

import com.lighthouse.beep.model.user.AuthProvider

data class OAuthRequest(
    val provider: AuthProvider = AuthProvider.NONE,
    val accessToken: String = "",
)
