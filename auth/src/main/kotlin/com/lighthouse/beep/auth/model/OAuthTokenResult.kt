package com.lighthouse.beep.auth.model

internal sealed interface OAuthTokenResult {
    data class Success(val accessToken: String) : OAuthTokenResult

    data class Canceled(
        val code: String = ERROR_CODE_UNKNOWN,
        val description: String = "",
    ) : OAuthTokenResult

    data class Failed(val throwable: Throwable) : OAuthTokenResult

    companion object {
        const val ERROR_CODE_UNKNOWN = "unknown"
    }
}
