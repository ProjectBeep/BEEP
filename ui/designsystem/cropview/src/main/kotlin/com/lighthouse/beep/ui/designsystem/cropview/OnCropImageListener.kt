package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.Bitmap
import android.graphics.RectF

interface OnCropImageListener {
    fun onCrop(croppedBitmap: Bitmap?, croppedRect: RectF?)
}
