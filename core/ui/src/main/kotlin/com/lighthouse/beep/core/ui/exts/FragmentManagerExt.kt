package com.lighthouse.beep.core.ui.exts

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

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
    try {
        val dialog = findFragmentByTag(tag) as? DialogFragment
        if (dialog?.isAdded == true) {
            dialog.dismiss()
        }
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }
}

fun FragmentActivity.replace(containerId: Int, tag: String, factoryProducer: () -> Fragment) {
    supportFragmentManager.replace(containerId, tag, factoryProducer)
}

fun Fragment.replace(containerId: Int, tag: String, factoryProducer: () -> Fragment) {
    childFragmentManager.replace(containerId, tag, factoryProducer)
}

fun FragmentManager.replace(containerId: Int, tag: String, factoryProducer: () -> Fragment) {
    val fragment = findFragmentByTag(tag) ?: factoryProducer()
    if (fragment.isAdded) {
        return
    }
    commit {
        replace(containerId, fragment, tag)
    }
}