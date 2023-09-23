package com.lighthouse.beep.core.ui.exts

import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.utils.throttle.OnThrottleClickListener

fun ComponentActivity.createThrottleClickListener(
    lifecycleOwner: LifecycleOwner = this,
    throttleTime: Long = OnThrottleClickListener.DEFAULT_THROTTLE_TIME,
    listener: suspend () -> Unit,
) : View.OnClickListener = OnThrottleClickListener(lifecycleOwner, throttleTime, listener)