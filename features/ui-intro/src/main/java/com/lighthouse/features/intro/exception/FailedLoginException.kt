package com.lighthouse.features.intro.exception

internal class FailedLoginException(
    message: String = "로그인에 실패 했습니다."
) : Exception(message = message)
