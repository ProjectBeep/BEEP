package com.lighthouse.beep.auth.kakao

sealed interface KakaoTokenResult {

    data class Success(val accessToken: String) : KakaoTokenResult

    data object Canceled : KakaoTokenResult

    data class Failed(val throwable: Throwable) : KakaoTokenResult
}
