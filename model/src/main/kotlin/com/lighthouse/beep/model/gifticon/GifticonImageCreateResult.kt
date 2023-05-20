package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri

data class GifticonImageCreateResult(
    val outputOriginUri: Uri,
    val outputCropUri: Uri,
    val outputCropRect: Rect,
)
