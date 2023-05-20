package com.lighthouse.beep.library.recognizer.model

import java.util.Date

data class ExpiredParserResult(
    val expired: Date,
    val filtered: List<String>,
)
