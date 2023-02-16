package com.lighthouse.features.intro.exception

internal class FailedSaveLoginUserException(
    message: String = "로그인 정보 저장에 실패 했습니다."
) : Exception(message)
