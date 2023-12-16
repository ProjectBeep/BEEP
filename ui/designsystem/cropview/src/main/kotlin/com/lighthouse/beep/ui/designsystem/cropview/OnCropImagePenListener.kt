package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.RectF

internal interface OnCropImagePenListener {

    fun onPenTouchStart()

    fun onPenTouchComplete(penCropRect: RectF)
}
