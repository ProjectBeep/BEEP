package com.lighthouse.features.common.utils.throttle

import android.view.View

fun View.onThrottleClick(
    duration: Long = 300L,
    onClick: (v: View) -> Unit
) {
    setOnClickListener(object : OnThrottleClickListener(duration) {
        override fun onThrottleClick(view: View) {
            onClick(view)
        }
    })
}
