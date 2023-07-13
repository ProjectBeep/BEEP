package com.lighthouse.beep.auth.model

import com.lighthouse.beep.model.deviceconfig.AuthProvider

data class OAuthRequest(
    val provider: AuthProvider = AuthProvider.NONE,
    val accessToken: String = "",
)
