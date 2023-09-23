package com.lighthouse.beep.core.ui.exts

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment

fun OnBackPressedCallback.onBackPressDelegate(fragment: Fragment, recovery: Boolean = false) {
    onBackPressDelegate(fragment.requireActivity(), recovery)
}

fun OnBackPressedCallback.onBackPressDelegate(activity: ComponentActivity, recovery: Boolean = false) {
    onBackPressDelegate(activity.onBackPressedDispatcher, recovery)
}

fun OnBackPressedCallback.onBackPressDelegate(dispatcher: OnBackPressedDispatcher, recovery: Boolean = false) {
    isEnabled = false
    dispatcher.onBackPressed()
    isEnabled = recovery
}