package com.lighthouse.beep.library.recognizer.model

import com.google.mlkit.vision.barcode.common.Barcode

data class BarcodeInfo(
    val type: Int = Barcode.FORMAT_CODE_128,
    val barcode: String = "",
) {
    val isNone: Boolean
        get() = this == None || barcode.isEmpty()

    companion object {
        val None = BarcodeInfo()
    }
}