package com.lighthouse.beep.model.gallery

import java.util.Date

data class GalleryRecognize(
    val imagePath: String,
    val dateAdded: Date,
    val isGifticon: Boolean,
)