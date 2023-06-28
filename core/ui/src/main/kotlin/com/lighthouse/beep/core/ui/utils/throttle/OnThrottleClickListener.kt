package com.lighthouse.beep.core.ui.utils.throttle

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class OnThrottleClickListener(
    private val lifecycleOwner: LifecycleOwner,
    private val throttleTime: Long = DEFAULT_THROTTLE_TIME,
    private val listener: suspend () -> Unit,
) : View.OnClickListener {

    private var lastRunTime: Long = 0L

    override fun onClick(view: View) {
        if (lastRunTime + throttleTime > System.currentTimeMillis()) {
            return
        }

        lastRunTime = System.currentTimeMillis()
        lifecycleOwner.lifecycleScope.launch {
            listener()
        }
    }

    companion object {
        const val DEFAULT_THROTTLE_TIME = 400L
    }
}
