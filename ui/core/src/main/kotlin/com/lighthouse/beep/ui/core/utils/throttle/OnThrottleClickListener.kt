package com.lighthouse.beep.ui.core.utils.throttle

import android.view.View

class OnThrottleClickListener(
    private val throttleTime: Long = THROTTLE_TIME,
    private val listener: View.OnClickListener,
) : View.OnClickListener {

    private var recentClickTime: Long = 0L

    private val isSafe: Boolean
        get() = System.currentTimeMillis() - recentClickTime > throttleTime

    override fun onClick(view: View) {
        if (isSafe) {
            recentClickTime = System.currentTimeMillis()
            listener.onClick(view)
        }
    }

    companion object {
        const val THROTTLE_TIME = 300L
    }
}
