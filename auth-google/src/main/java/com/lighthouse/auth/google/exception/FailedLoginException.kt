package com.lighthouse.auth.google.exception

class FailedLoginException(
    message: String = "로그인에 실패 했습니다."
) : Exception(message)
