package com.lighthouse.beep.ui.core.exts

import android.view.View
import com.lighthouse.beep.ui.core.utils.throttle.OnThrottleClickListener

fun View.setOnThrottleClickListener(
    throttleTime: Long = OnThrottleClickListener.THROTTLE_TIME,
    listener: View.OnClickListener,
) {
    setOnClickListener(
        OnThrottleClickListener(throttleTime = throttleTime, listener = listener),
    )
}
