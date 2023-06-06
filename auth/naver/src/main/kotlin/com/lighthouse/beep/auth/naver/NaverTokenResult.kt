package com.lighthouse.beep.auth.naver

sealed interface NaverTokenResult {

    data class Success(val token: String) : NaverTokenResult

    object Canceled : NaverTokenResult

    data class Failed(val throwable: Throwable) : NaverTokenResult
}
