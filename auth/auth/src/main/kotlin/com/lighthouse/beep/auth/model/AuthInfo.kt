package com.lighthouse.beep.auth.model

import android.net.Uri

data class AuthInfo(
    val provider: AuthProvider = AuthProvider.NONE,
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: Uri? = null,
)
