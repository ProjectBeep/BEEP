package com.lighthouse.beep.core.ui.utils.throttle

import android.view.View

open class OnThrottleClickListener(
    private val throttleTime: Long = DEFAULT_THROTTLE_TIME,
    private val listener: () -> Unit,
) : View.OnClickListener {

    private var lastRunTime: Long = 0L

    override fun onClick(view: View) {
        if (lastRunTime + throttleTime > System.currentTimeMillis()) {
            return
        }

        lastRunTime = System.currentTimeMillis()
        listener()
    }

    companion object {
        const val DEFAULT_THROTTLE_TIME = 400L
    }
}
