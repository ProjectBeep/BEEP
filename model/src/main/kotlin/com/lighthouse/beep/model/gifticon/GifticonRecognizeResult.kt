package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri
import java.util.Date

data class GifticonRecognizeResult(
    val imageWidth: Int,
    val imageHeight: Int,
    val name: String,
    val brandName: String,
    val barcode: String,
    val expiredAt: Date,
    val isCashCard: Boolean,
    val balance: Int,
    val originUri: Uri,
    val croppedUri: Uri?,
    val croppedRect: Rect?,
)
