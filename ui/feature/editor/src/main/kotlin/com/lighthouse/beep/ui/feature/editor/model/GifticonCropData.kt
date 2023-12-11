package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Rect
import com.lighthouse.beep.core.common.exts.EMPTY_RECT

internal data class GifticonCropData(
    val rect: Rect = EMPTY_RECT,
    val zoom: Float = 0f,
) {
    companion object {
        val None = GifticonCropData()
    }
}