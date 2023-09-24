package com.lighthouse.beep.ui.feature.home.model

import android.net.Uri
import com.lighthouse.beep.core.common.exts.calculateDDay
import java.util.Date

data class ExpiredGifticonItem(
    val id: Long,
    val thumbnailUri: Uri,
    val brand: String,
    val name: String,
    val expiredDate: Date,
    val createdDate: Date,
) {
    val dday: Int
        get() = expiredDate.calculateDDay()
}