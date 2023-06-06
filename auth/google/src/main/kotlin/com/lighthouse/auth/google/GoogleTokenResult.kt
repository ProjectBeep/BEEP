package com.lighthouse.auth.google

sealed interface GoogleTokenResult {

    data class Success(val idToken: String) : GoogleTokenResult

    object Canceled : GoogleTokenResult

    data class Failed(val throwable: Throwable) : GoogleTokenResult
}
