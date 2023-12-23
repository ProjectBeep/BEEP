package com.lighthouse.beep.data.model.gifticon

import android.graphics.Rect
import android.net.Uri

data class GifticonThumbnailImageResult(
    val thumbnailUri: Uri,
    val thumbnailRect: Rect,
)