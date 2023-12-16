package com.lighthouse.beep.permission.ext

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun ComponentActivity.checkSelfPermissions(permissions: Array<String>): Boolean {
    return permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
}

fun Fragment.checkSelfPermissions(permissions: Array<String>): Boolean {
    return permissions.all { context?.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
}

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