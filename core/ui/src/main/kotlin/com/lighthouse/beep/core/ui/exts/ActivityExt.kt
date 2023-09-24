package com.lighthouse.beep.core.ui.exts

import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.utils.throttle.OnLifecycleThrottleClickListener
import com.lighthouse.beep.core.ui.utils.throttle.OnThrottleClickListener

fun ComponentActivity.createThrottleClickListener(
    lifecycleOwner: LifecycleOwner = this,
    throttleTime: Long = OnThrottleClickListener.DEFAULT_THROTTLE_TIME,
    listener: suspend () -> Unit,
): View.OnClickListener = OnLifecycleThrottleClickListener(lifecycleOwner, throttleTime, listener)

fun ComponentActivity.setUpSystemInsetsPadding(root: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        root.doOnAttach {
            val windowInsets = it.rootWindowInsets
            val insets =
                windowInsets.getInsets(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.updatePadding(top = insets.top, bottom = insets.bottom)
        }
    } else {
        root.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.updatePadding(
                top = windowInsets.systemWindowInsetTop,
                bottom = windowInsets.systemWindowInsetBottom
            )
            view.setOnApplyWindowInsetsListener(null)
            return@setOnApplyWindowInsetsListener windowInsets
        }
    }
}

fun ComponentActivity.checkSelfPermissions(permissions: Array<String>): Boolean {
    return permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
}