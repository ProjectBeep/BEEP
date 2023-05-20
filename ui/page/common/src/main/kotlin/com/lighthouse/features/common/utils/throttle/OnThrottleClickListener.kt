package com.lighthouse.features.common.utils.throttle

import android.view.View

abstract class OnThrottleClickListener(duration: Long = THROTTLE_TIME) : View.OnClickListener {

    private var recentClickTime: Long = 0L

    private val isSafe: Boolean
        get() = System.currentTimeMillis() - recentClickTime > THROTTLE_TIME

    override fun onClick(view: View) {
        if (isSafe) {
            recentClickTime = System.currentTimeMillis()
            onThrottleClick(view)
        }
    }

    abstract fun onThrottleClick(view: View)

    companion object {
        private const val THROTTLE_TIME = 300L
    }
}
