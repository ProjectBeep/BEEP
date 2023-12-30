package com.lighthouse.beep.model.gifticon

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GifticonDetail(
    val id: Long,
    val userId: String,
    val isCashCard: Boolean,
    val remainCash: Int,
    val totalCash: Int,
    val thumbnail: GifticonThumbnail,
    val name: String,
    val displayBrand: String,
    val barcode: String,
    val memo: String,
    val isUsed: Boolean,
    val expireAt: Date,
) {

    val isExpired: Boolean
        get() = Date() >= expireAt

    val formattedExpiredDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(expireAt)
        }
}
