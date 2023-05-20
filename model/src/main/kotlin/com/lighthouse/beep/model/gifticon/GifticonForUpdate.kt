package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri
import java.util.Date

data class GifticonForUpdate(
    val id: Long,
    val userId: String,
    val newCroppedUri: Uri,
    val newCroppedRect: Rect,
    val croppedUri: Uri,
    val croppedRect: Rect,
    val originUri: Uri,
    val name: String,
    val brandName: String,
    val barcode: String,
    val isCashCard: Boolean,
    val totalCash: Int,
    val remainCash: Int,
    val memo: String,
    val isUsed: Boolean,
    val expireAt: Date,
    val createdAt: Date,
) {
    val isUpdatedImage
        get() = croppedUri.toString() != newCroppedUri.toString()
}
