package com.lighthouse.beep.core.ui.exts

import android.annotation.SuppressLint
import android.graphics.Point
import android.view.View
import com.lighthouse.beep.core.ui.utils.throttle.OnThrottleClickListener

fun View.setOnThrottleClickListener(
    throttleTime: Long = OnThrottleClickListener.DEFAULT_THROTTLE_TIME,
    listener: () -> Unit,
) {
    setOnClickListener(OnThrottleClickListener(throttleTime, listener))
}

@SuppressLint("ClickableViewAccessibility")
fun View.preventTouchPropagation() {
    setOnTouchListener { _, _ -> true }
}

fun View.getViewPointOnScreen(): Point {
    val location = intArrayOf(0, 0)
    getLocationOnScreen(location)
    return Point(location[0], location[1])
}

val View.viewWidth: Int
    get() = when {
        width > 0 -> width
        measuredWidth > 0 -> measuredWidth
        layoutParams.width > 0 -> layoutParams.width
        else -> 0
    }

val View.viewHeight: Int
    get() = when {
        height > 0 -> height
        measuredHeight > 0 -> measuredHeight
        layoutParams.height > 0 -> layoutParams.height
        else -> 0
    }