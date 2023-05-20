package com.lighthouse.beep.library.recognizer.recognizer.smilecon

import com.lighthouse.beep.library.recognizer.parser.BaseParser

internal class SmileConParser : BaseParser() {

    override val keywordText = listOf(
        "스마일콘",
        "오피스콘",
        "smile",
    )
}
