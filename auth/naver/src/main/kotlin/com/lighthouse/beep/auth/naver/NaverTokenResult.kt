package com.lighthouse.beep.auth.naver

sealed interface NaverTokenResult {

    data class Success(val accessToken: String) : NaverTokenResult

    data class Canceled(val code: String, val description: String) : NaverTokenResult

    data class Failed(val throwable: Throwable) : NaverTokenResult
}
