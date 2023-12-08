package com.lighthouse.beep.core.ui.keyboard

import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class KeyboardProvider(
    activity: ComponentActivity
) : PopupWindow(), ViewTreeObserver.OnGlobalLayoutListener {

    private val view = View(activity)
    private var originHeight = -1

    private var onKeyboardHeightListener: OnKeyboardHeightListener? = null

    fun setOnKeyboardHeightListener(listener: OnKeyboardHeightListener) {
        onKeyboardHeightListener = listener
    }

    init {
        contentView = view

        inputMethodMode = INPUT_METHOD_NEEDED

        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE

            view.setOnApplyWindowInsetsListener { _, insets ->
                val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                onKeyboardHeightListener?.onHeightChanged(imeHeight - navigationBarHeight)
                insets
            }
        } else {
            softInputMode =
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            view.viewTreeObserver.addOnGlobalLayoutListener(this)
        }

        showAtLocation(activity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
    }

    fun release() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.setOnApplyWindowInsetsListener(null)
        } else {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onGlobalLayout() {
        if (view.height > originHeight) {
            originHeight = view.height
        }
        val visibleFrameSize = Rect().apply {
            view.getWindowVisibleDisplayFrame(this)
        }

        val visibleFrameHeight = visibleFrameSize.bottom - visibleFrameSize.top
        val keyboardHeight = originHeight - visibleFrameHeight
        onKeyboardHeightListener?.onHeightChanged(keyboardHeight)
    }
}

fun interface OnKeyboardHeightListener {
    fun onHeightChanged(height: Int)
}

class FragmentKeyboardProvider(
    private val fragment: Fragment,
) : ReadOnlyProperty<Fragment, KeyboardProvider> {

    private var keyboardProvider: KeyboardProvider? = null

    private val fragmentObserver = object : DefaultLifecycleObserver {
        private val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { viewLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    keyboardProvider?.release()
                    if (keyboardProvider?.isShowing == true) {
                        keyboardProvider?.dismiss()
                    }
                    keyboardProvider = null
                }

                override fun onStart(owner: LifecycleOwner) {
                    if (keyboardProvider == null) {
                        keyboardProvider = KeyboardProvider(fragment.requireActivity())
                    }
                }
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

    init {
        fragment.lifecycle.addObserver(fragmentObserver)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): KeyboardProvider {
        return keyboardProvider ?: KeyboardProvider(fragment.requireActivity()).also {
            keyboardProvider = it
        }
    }
}

class ActivityKeyboardProvider(
    private val activity: ComponentActivity,
) : ReadOnlyProperty<ComponentActivity, KeyboardProvider> {
    private var keyboardProvider: KeyboardProvider? = null

    private val activityObserver = object : DefaultLifecycleObserver {
        override fun onStop(owner: LifecycleOwner) {
            keyboardProvider?.release()
            if (keyboardProvider?.isShowing == true) {
                keyboardProvider?.dismiss()
            }
            keyboardProvider = null
        }

        override fun onStart(owner: LifecycleOwner) {
            if (keyboardProvider == null) {
                keyboardProvider = KeyboardProvider(activity)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            activity.lifecycle.removeObserver(this)
        }
    }

    init {
        activity.lifecycle.addObserver(activityObserver)
    }

    override fun getValue(thisRef: ComponentActivity, property: KProperty<*>): KeyboardProvider {
        return keyboardProvider ?: KeyboardProvider(activity).also {
            keyboardProvider = it
        }
    }
}

fun Fragment.keyboardProviders() =
    FragmentKeyboardProvider(this)

fun ComponentActivity.keyboardProviders() =
    ActivityKeyboardProvider(this)