package com.lighthouse.beep.library.recognizer.model

import android.graphics.Bitmap
import android.graphics.Rect
import java.util.Date

data class GifticonRecognizeInfo(
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val name: String = "",
    val brand: String = "",
    val expiredAt: Date = Date(0),
    val barcodeType: Int = 0,
    val barcode: String = "",
    val isCashCard: Boolean = false,
    val balance: Int = 0,
    val candidate: List<String> = listOf(),
    val croppedImage: Bitmap? = null,
    val croppedRect: Rect = Rect(),
)
