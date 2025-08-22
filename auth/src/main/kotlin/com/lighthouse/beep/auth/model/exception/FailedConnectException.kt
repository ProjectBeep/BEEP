package com.lighthouse.beep.auth.model.exception

class FailedConnectException(
    message: String = "서버와 연결을 실패 했습니다.",
) : Exception(message)
