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
    try {
        var dialog = findFragmentByTag(tag) as? DialogFragment
        if (dialog == null) {
            dialog = factoryProducer()
        }
        if (!dialog.isAdded) {
            dialog.show(this, tag)
        }
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }
}

fun FragmentActivity.dismiss(tag: String) {
    supportFragmentManager.dismiss(tag)
}

fun Fragment.dismiss(tag: String) {
    childFragmentManager.dismiss(tag)
}

fun FragmentManager.dismiss(tag: String) {
    val dialog = findFragmentByTag(tag) as? DialogFragment
    if (dialog?.isAdded == true) {
        dialog.dismiss()
    }
}