package com.lighthouse.beep.core.ui.exts

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

fun FragmentActivity.show(tag: String, factoryProducer: () -> DialogFragment) {
    supportFragmentManager.show(tag, factoryProducer)
}

fun Fragment.show(tag: String, factoryProducer: () -> DialogFragment) {
    childFragmentManager.show(tag, factoryProducer)
}

fun FragmentManager.show(tag: String, factoryProducer: () -> DialogFragment) {
    var dialog = findFragmentByTag(tag) as? DialogFragment
    if (dialog == null) {
        dialog = factoryProducer()
    }
    dialog.show(this, tag)
}

fun FragmentActivity.hide(tag: String) {
    supportFragmentManager.hide(tag)
}

fun Fragment.hide(tag: String) {
    childFragmentManager.hide(tag)
}

fun FragmentManager.hide(tag: String) {
    val dialog = findFragmentByTag(tag) as? DialogFragment
    dialog?.dismiss()
}