package com.lighthouse.beep.core.ui.recyclerview.viewholder

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.doOnAttach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class LifecycleViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    protected val context: Context
        get() = itemView.context

    private val resource: Resources
        get() = itemView.resources

    fun getColor(@ColorRes resId: Int): Int = resource.getColor(resId, null)

    fun getString(@StringRes resId: Int): String = resource.getString(resId)

    private var lifecycleOwner: LifecycleOwner? = null
    private var boundItem: T? = null

    private var viewHolderScope: CoroutineScope? = null

    init {
        view.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                val viewTreeLifecycleOwner = v.findViewTreeLifecycleOwner()
                val boundItem = boundItem
                lifecycleOwner = viewTreeLifecycleOwner
                if(boundItem != null && viewTreeLifecycleOwner != null) {
                    onCollectState(viewTreeLifecycleOwner, boundItem)
                }
            }

            override fun onViewDetachedFromWindow(v: View) {
                lifecycleOwner = null
                boundItem = null
            }
        })
    }

    @CallSuper
    open fun bind(item: T) {
        boundItem = item
        itemView.doOnAttach {
            viewHolderScope?.cancel()
            viewHolderScope = CoroutineScope(Dispatchers.Main.immediate)
            val lifecycleOwner = lifecycleOwner ?: return@doOnAttach
            onCollectState(lifecycleOwner, item)
        }
        onSetUpClickEvent(item)
    }

    open fun onSetUpClickEvent(item: T) = Unit

    open fun onCollectState(lifecycleOwner: LifecycleOwner, item: T) = Unit

    private fun <T> Flow<T>.flowWithLifecycle(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED
    ): Flow<T> = flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend (item: T) -> Unit,
    ) {
        viewHolderScope?.launch {
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it)
            }
        }
    }

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend (item: T, init: Boolean) -> Unit,
    ) {
        var init = true
        viewHolderScope?.launch {
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it, init)
                if (init) {
                    init = false
                }
            }
        }
    }

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        default: T,
        block: suspend (item: T) -> Unit
    ) {
        viewHolderScope?.launch {
            block(default)
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it)
            }
        }
    }

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        default: T,
        block: suspend (item: T, init: Boolean) -> Unit
    ) {
        var init = true
        viewHolderScope?.launch {
            block(default, init)
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it, init)
                if (init) {
                    init = false
                }
            }
        }
    }

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        defaultBlock: suspend () -> Unit,
        block: suspend (item: T) -> Unit,
    ) {
        viewHolderScope?.launch {
            defaultBlock()
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it)
            }
        }
    }

    protected fun <T> Flow<T>.collect(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        defaultBlock: suspend () -> Unit,
        block: suspend (item: T, init: Boolean) -> Unit,
    ) {
        var init = true
        viewHolderScope?.launch {
            defaultBlock()
            flowWithLifecycle(lifecycleOwner, minActiveState).collect {
                block(it, init)
                if (init) {
                    init = false
                }
            }
        }
    }
}