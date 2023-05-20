package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri

data class GifticonImageUpdateResult(
    val outputCropUri: Uri,
    val outputCropRect: Rect,
)
