package com.lighthouse.beep.core.ui.exts

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.utils.throttle.OnThrottleClickListener

fun Fragment.onBackPressDelegate() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}

fun Fragment.addBackPressCallback(lifecycleOwner: LifecycleOwner = viewLifecycleOwner, callback: OnBackPressedCallback) {
    requireActivity().onBackPressedDispatcher.addCallback(lifecycleOwner, callback)
}

fun Fragment.createThrottleClickListener(
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    throttleTime: Long = OnThrottleClickListener.DEFAULT_THROTTLE_TIME,
    listener: suspend () -> Unit,
) : View.OnClickListener = OnThrottleClickListener(lifecycleOwner, throttleTime, listener)