package com.lighthouse.beep.model.gifticon

import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import java.util.Date

data class GifticonEditInfo(
    val thumbnailType: String,
    val thumbnailBuiltInCode: String,
    val thumbnailBitmap: Bitmap?,
    val thumbnailUri: Uri?,
    val thumbnailRect: Rect,
    val gifticonUri: Uri?,
    val originUri: Uri,
    val imagePath: String,
    val name: String,
    val displayBrand: String,
    val barcode: String,
    val isCashCard: Boolean,
    val totalCash: Int,
    val remainCash: Int,
    val memo: String,
    val expireAt: Date,
)