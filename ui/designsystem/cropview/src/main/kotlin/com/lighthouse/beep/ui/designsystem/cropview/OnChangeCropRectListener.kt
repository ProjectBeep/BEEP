package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.Bitmap
import android.graphics.Rect

fun interface OnChangeCropRectListener {
    fun onChange(originBitmap: Bitmap, rect: Rect, zoom: Float)
}
