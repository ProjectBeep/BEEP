package com.lighthouse.beep.core.ui.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class VisibleLifecycleOwner : LifecycleOwner {

    override val lifecycle: LifecycleRegistry = LifecycleRegistry(this)

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycle.handleLifecycleEvent(event)
    }
}