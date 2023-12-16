package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.RectF

internal interface OnCropImagePenListener {

    fun onPenTouchComplete(penCropRect: RectF)
}
