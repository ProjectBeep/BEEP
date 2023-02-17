package com.lighthouse.auth.exception

class FailedLoginException(
    message: String = "로그인에 실패 했습니다."
) : Exception(message)
