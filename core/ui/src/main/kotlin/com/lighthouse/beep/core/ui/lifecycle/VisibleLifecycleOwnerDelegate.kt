package com.lighthouse.beep.core.ui.lifecycle

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentVisibleLifecycleOwnerDelegate(
    private val fragment: Fragment
) : ReadOnlyProperty<Fragment, VisibleLifecycleOwner> {

    private var visibleLifecycleOwner: VisibleLifecycleOwner? = null

    private val fragmentObserver = object : DefaultLifecycleObserver {
        private val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { viewLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.addObserver(LifecycleEventObserver { _, event ->
                Log.d(
                    "ComponentLogger",
                    "[Visible] : event - $event, hidden : ${fragment.isHidden} - ${fragment.javaClass.simpleName}(${fragment.hashCode()}):(${visibleLifecycleOwner.hashCode()})"
                )
                when (event) {
                    Lifecycle.Event.ON_START,
                    Lifecycle.Event.ON_RESUME,
                    Lifecycle.Event.ON_PAUSE -> {
                        if (fragment.isHidden) {
                            visibleLifecycleOwner?.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                        } else {
                            visibleLifecycleOwner?.handleLifecycleEvent(event)
                        }
                    }

                    else -> {
                        visibleLifecycleOwner?.handleLifecycleEvent(event)
                    }
                }
                Log.d(
                    "ComponentLogger",
                    "[Visible State] ${visibleLifecycleOwner?.lifecycle?.currentState} - ${fragment.javaClass.simpleName}(${fragment.hashCode()}):(${visibleLifecycleOwner.hashCode()})"
                )
            })
        }

        override fun onCreate(owner: LifecycleOwner) {
            fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
            fragment.lifecycle.removeObserver(this)
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VisibleLifecycleOwner {
        visibleLifecycleOwner?.let {
            return it
        }
        fragment.lifecycle.addObserver(fragmentObserver)
        return VisibleLifecycleOwner().also {
            visibleLifecycleOwner = it
        }
    }
}

fun Fragment.visibleLifecycleOwner() = FragmentVisibleLifecycleOwnerDelegate(this)