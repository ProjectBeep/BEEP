package com.lighthouse.auth.google

sealed interface GoogleTokenResult {

    data class Success(val idToken: String) : GoogleTokenResult

    data object Canceled : GoogleTokenResult

    data class Failed(val throwable: Throwable) : GoogleTokenResult
}
