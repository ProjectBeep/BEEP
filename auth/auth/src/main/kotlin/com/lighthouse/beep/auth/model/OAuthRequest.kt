package com.lighthouse.beep.auth.model

data class OAuthRequest(
    val provider: AuthProvider = AuthProvider.NONE,
    val accessToken: String = "",
)
