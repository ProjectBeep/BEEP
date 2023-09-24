package com.lighthouse.beep.core.ui.utils.throttle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class OnLifecycleThrottleClickListener(
    private val lifecycleOwner: LifecycleOwner,
    throttleTime: Long = DEFAULT_THROTTLE_TIME,
    private val listener: suspend () -> Unit,
) : OnThrottleClickListener(throttleTime, {
    lifecycleOwner.lifecycleScope.launch {
        listener()
    }
})
