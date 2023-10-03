package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.Bitmap
import android.graphics.RectF

fun interface OnChangeCropRectListener {
    fun onChange(originBitmap: Bitmap, rect: RectF)
}
