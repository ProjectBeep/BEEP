package com.lighthouse.beep.data.model.gifticon

import android.graphics.Bitmap
import android.net.Uri

data class GifticonOriginImageResult(
    val gifticonUri: Uri,
    val gifticonBitmap: Bitmap,
)