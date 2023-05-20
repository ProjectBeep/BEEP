package com.lighthouse.beep.core.exts

import android.graphics.Rect

fun Rect.scale(scale: Float): Rect {
    if (scale == 1f) {
        return this
    }
    return Rect(
        (left * scale + 0.5f).toInt(),
        (top * scale + 0.5f).toInt(),
        (right * scale + 0.5f).toInt(),
        (bottom * scale + 0.5f).toInt(),
    )
}
