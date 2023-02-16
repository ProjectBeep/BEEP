package com.lighthouse.features.intro.exception

internal class NotFoundEmailException(
    message: String = "이메일을 가져오는데 실패 했습니다."
) : Exception(message)
