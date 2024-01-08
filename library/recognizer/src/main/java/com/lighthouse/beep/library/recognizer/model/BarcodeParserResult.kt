package com.lighthouse.beep.library.recognizer.model

data class BarcodeParserResult(
    val type: Int,
    val barcode: String,
    val filtered: List<String>,
)
