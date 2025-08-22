package com.lighthouse.beep.core.ui.keyboard

import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class KeyboardProvider(
    activity: ComponentActivity
) : PopupWindow(), ViewTreeObserver.OnGlobalLayoutListener {

    private val view = View(activity)
    private var originHeight = -1

    private var onKeyboardHeightListener: OnKeyboardHeightListener? = null

    fun setOnKeyboardHeightListener(listener: OnKeyboardHeightListener?) {
        onKeyboardHeightListener = listener
    }

    init {
        contentView = view

        inputMethodMode = INPUT_METHOD_NEEDED

        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
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
    private val listener: OnKeyboardHeightListener?,
) {

    private var keyboardHeightProvider: KeyboardProvider? = null

    private val fragmentObserver = object: DefaultLifecycleObserver {
        private val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { viewLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    if (keyboardHeightProvider == null) {
                        keyboardHeightProvider = KeyboardProvider(fragment.requireActivity()).apply {
                            setOnKeyboardHeightListener(listener)
                        }
                    }
                }

                override fun onStop(owner: LifecycleOwner) {
                    keyboardHeightProvider?.release()
                    if (keyboardHeightProvider?.isShowing == true) {
                        keyboardHeightProvider?.dismiss()
                    }
                    keyboardHeightProvider = null
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
}

class ActivityKeyboardProvider(
    private val activity: ComponentActivity,
    private val listener: OnKeyboardHeightListener?,
) {

    private var keyboardHeightProvider: KeyboardProvider? = null

    private val activityObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            activity.lifecycle.removeObserver(this)
        }

        override fun onStart(owner: LifecycleOwner) {
            if (keyboardHeightProvider == null) {
                keyboardHeightProvider = KeyboardProvider(activity).apply {
                    setOnKeyboardHeightListener(listener)
                }
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            keyboardHeightProvider?.release()
            if(keyboardHeightProvider?.isShowing == true) {
                keyboardHeightProvider?.dismiss()
            }
            keyboardHeightProvider = null
        }
    }

    init {
        activity.lifecycle.addObserver(activityObserver)
    }
}

fun Fragment.addKeyboardHeightCallback(listener: OnKeyboardHeightListener?) {
    FragmentKeyboardProvider(this, listener)
}

fun ComponentActivity.addKeyboardHeightCallback(listener: OnKeyboardHeightListener?) =
    ActivityKeyboardProvider(this, listener)