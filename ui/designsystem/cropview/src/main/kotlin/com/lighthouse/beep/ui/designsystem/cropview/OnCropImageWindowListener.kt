package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.RectF

internal interface OnCropImageWindowListener {

    fun onWindowMove(unconsumedX: Float, unconsumedY: Float, boundRect: RectF)

    fun onWindowResized(viewCropRect: RectF)

    fun onWindowTouchComplete(viewCropRect: RectF)
}
