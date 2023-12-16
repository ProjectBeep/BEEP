package com.lighthouse.beep.permission.ext

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.checkSelfPermissions

fun ComponentActivity.setUpRequirePermission(permissions: Array<String>) {
    lifecycle.addObserver(object: DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            if (!checkSelfPermissions(permissions)) {
                finish()
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
        }
    })
}