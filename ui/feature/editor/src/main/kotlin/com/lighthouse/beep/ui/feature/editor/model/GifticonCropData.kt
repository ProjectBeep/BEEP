package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.RectF
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F

internal data class GifticonCropData(
    val rect: RectF = EMPTY_RECT_F,
    val zoom: Float = 0f,
) {
    companion object {
        val None = GifticonCropData()
    }
}