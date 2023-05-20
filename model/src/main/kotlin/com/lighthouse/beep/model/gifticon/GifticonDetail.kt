package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri
import java.util.Date

data class GifticonDetail(
    val id: Long,
    val userId: String,
    val croppedUri: Uri?,
    val croppedRect: Rect,
    val originUri: Uri?,
    val name: String,
    val displayBrand: String,
    val barcode: String,
    val isCashCard: Boolean,
    val totalCash: Int,
    val remainCash: Int,
    val memo: String,
    val isUsed: Boolean,
    val expireAt: Date,
    val createdAt: Date,
)
