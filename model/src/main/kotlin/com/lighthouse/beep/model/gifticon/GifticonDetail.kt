package com.lighthouse.beep.model.gifticon

import java.util.Date

data class GifticonDetail(
    val id: Long,
    val userId: String,
    val type: GifticonType,
    val thumbnail: GifticonThumbnail,
    val name: String,
    val displayBrand: String,
    val barcode: String,
    val memo: String,
    val isUsed: Boolean,
    val expireAt: Date,
)
