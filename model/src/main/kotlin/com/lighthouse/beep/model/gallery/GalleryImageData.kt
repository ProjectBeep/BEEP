package com.lighthouse.beep.model.gallery

import java.util.Date

data class GalleryImageData(
    val imagePath: String,
    val addedDate: Date,
    val isGifticon: Boolean,
    val addedGifticonId: Long?,
)