package com.lighthouse.auth.google.exception

class FailedSignOutException(
    message: String = "로그 아웃에 실패 했습니다."
) : Exception(message)
