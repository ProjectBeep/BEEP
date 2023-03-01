package com.lighthouse.beep.model.exception.auth

class FailedSaveLoginUserException(
    message: String = "로그인 정보 저장에 실패 했습니다."
) : Exception(message)
