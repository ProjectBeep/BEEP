package com.lighthouse.beep.model.gifticon

import android.net.Uri
import java.util.Date

data class GifticonListItem(
    val id: String,
    val croppedUri: Uri?,
    val name: String,
    val displayBrand: String,
    val expireAt: Date,
    val isCashCard: Boolean,
    val totalCash: Int,
    val remainCash: Int,
    val dDay: Int,
)
