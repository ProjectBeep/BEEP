package com.lighthouse.beep.ui.designsystem.snackbar

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.animation.SimpleAnimatorListener
import com.lighthouse.beep.core.ui.exts.getString
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.designsystem.snackbar.databinding.SnackbarBeepBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.math.abs

@Suppress("unused")
class BeepSnackBar(context: Context) : ViewGroup(context) {

    companion object {
        private const val SHORT_DURATION_MS = 1500L
        private const val LONG_DURATION_MS = 2750L

        private const val DEFAULT_SWIPE_ANIMATION_DURATION = 180L

        private const val DEFAULT_SHOW_DURATION = 180L
        private const val DEFAULT_DISMISS_DURATION = 180L
    }

    var builder = Builder(context)

    private var viewScope = CoroutineScope(Dispatchers.Main.immediate)

    private val binding: SnackbarBeepBinding =
        SnackbarBeepBinding.inflate(LayoutInflater.from(context), this, true)

    private var onSnackBarDismissListener: OnSnackBarDismissListener? = null

    private var snackBarSwipeDismissAnimator: ViewPropertyAnimator? = null

    private val snackBarSwipeDismissListener = object : OnTouchListener {
        var touchStartX = 0f

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            if (!view.isEnabled) {
                return false
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStartX = event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    binding.root.translationX = event.rawX - touchStartX
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    val moveDistance = abs(binding.root.translationX)
                    val isShow = moveDistance < binding.root.width * 0.1f
                    startAnimation(isShow)
                }
            }
            return true
        }

        private fun startAnimation(isShow: Boolean) {
            val start = binding.root.translationX
            val end = when {
                isShow -> 0f
                binding.root.translationX < 0 -> -binding.root.width.toFloat()
                else -> binding.root.width.toFloat()
            }
            snackBarSwipeDismissAnimator?.cancel()
            snackBarSwipeDismissAnimator = binding.containerSnackBar.animate()
                .setDuration(DEFAULT_SWIPE_ANIMATION_DURATION)
                .setListener(object : SimpleAnimatorListener() {
                    override fun onAnimationStart(animator: Animator) {
                        binding.containerSnackBar.isEnabled = false
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        clearAnimation()
                    }

                    override fun onAnimationCancel(animator: Animator) {
                        clearAnimation()
                    }

                    private fun clearAnimation() {
                        snackBarSwipeDismissAnimator = null
                        binding.containerSnackBar.isEnabled = true
                        if (!isShow) {
                            dismiss()
                        }
                    }
                })
                .setUpdateListener {
                    binding.root.translationX =
                        start - (start - end) * it.animatedFraction
                }.also {
                    it.start()
                }
        }
    }

    private fun setOnSnackBarDismissListener(listener: OnSnackBarDismissListener?) {
        onSnackBarDismissListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpEnableSwipeDismiss() {
        if (builder.enableSwipeDismiss) {
            binding.containerSnackBar.setOnTouchListener(snackBarSwipeDismissListener)
        } else {
            binding.containerSnackBar.preventTouchPropagation()
        }
    }

    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            dismiss(runAnimation = false)
        }
    }

    private fun setUpObserveLifecycle() {
        builder.lifecycleOwner?.lifecycle?.addObserver(lifecycleObserver)
    }

    private fun removeObserveLifecycle() {
        builder.lifecycleOwner?.lifecycle?.removeObserver(lifecycleObserver)
    }

    private fun initializeSnackBarContent() {
        clipToPadding = false
        binding.containerSnackBar.updateLayoutParams<FrameLayout.LayoutParams> {
            leftMargin = builder.marginLeft
            topMargin = builder.marginTop
            rightMargin = builder.marginRight
            bottomMargin = builder.marginBottom
        }
        binding.text.text = context.getString(builder.text, builder.textResId)

        val state = builder.state
        binding.containerSnackBar.setBackgroundResource(state.backgroundResId)
        binding.icon.isVisible = state.isIconVisible
        if (state.isIconVisible) {
            binding.icon.setImageResource(state.iconResId)
            binding.icon.setColorFilter(context.getColor(state.iconColorResId))
        }
        binding.text.setTextColor(context.getColor(state.textColorResId))
        binding.iconAction.setColorFilter(context.getColor(state.actionColorResId))
        binding.textAction.setTextColor(context.getColor(state.actionColorResId))

        val action = builder.action
        binding.iconAction.isVisible = action is BeepSnackBarAction.Icon
        if (action is BeepSnackBarAction.Icon) {
            binding.iconAction.setImageResource(action.drawableResId)
            binding.iconAction.setOnThrottleClickListener {
                action.listener?.onActionClick()
                dismiss()
            }
        }

        binding.textAction.isVisible = action is BeepSnackBarAction.Text
        if (action is BeepSnackBarAction.Text) {
            binding.textAction.text = action.getText(context)
            binding.textAction.setOnThrottleClickListener {
                action.listener?.onActionClick()
                dismiss()
            }
        }
    }

    private fun initializeSnackBarListeners() {
        setUpEnableSwipeDismiss()
        setOnSnackBarDismissListener(builder.onSnackBarDismissListener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val child = getChildAt(0)
        measureChild(child, widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            doLayout()
        }
    }

    private fun doLayout() {
        val child = getChildAt(0)
        child.layout(left, bottom - child.measuredHeight, right, bottom)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!viewScope.isActive) {
            viewScope = CoroutineScope(Dispatchers.Main.immediate)
        }
    }

    override fun onDetachedFromWindow() {
        viewScope.cancel()
        super.onDetachedFromWindow()
    }

    private val duration: Long
        get() = when {
            builder.duration == Snackbar.LENGTH_LONG -> LONG_DURATION_MS
            builder.duration == Snackbar.LENGTH_SHORT -> SHORT_DURATION_MS
            builder.duration > 0L -> builder.duration.toLong()
            else -> LONG_DURATION_MS
        }

    private fun findRootView(): ViewGroup? {
        builder.rootView?.let {
            return it
        }
        val activity = context as? Activity
        return activity?.window?.decorView as? ViewGroup
    }

    private var isActive = true

    private var showAnimator: ViewPropertyAnimator? = null

    fun show() {
        clearAnimator()

        setUpObserveLifecycle()
        initializeSnackBarContent()
        initializeSnackBarListeners()

        isActive = true

        val rootView = findRootView() ?: return
        rootView.addView(this, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        showAnimator = animate()
            .setDuration(DEFAULT_SHOW_DURATION)
            .setUpdateListener {
                binding.root.alpha = it.animatedFraction
            }.also {
                it.start()
            }

        viewScope.launch {
            delay(duration)
            dismiss(runAnimation = true)
        }
    }

    private var dismissAnimator: ViewPropertyAnimator? = null

    private fun release() {
        if (isActive){
            return
        }

        removeObserveLifecycle()
        binding.root.translationX = 0f

        val parent = parent as? ViewGroup
        parent?.removeView(this)

        onSnackBarDismissListener?.onDismiss()
    }

    private fun clearAnimator() {
        snackBarSwipeDismissAnimator?.cancel()
        snackBarSwipeDismissAnimator = null
        showAnimator?.cancel()
        showAnimator = null
        dismissAnimator?.cancel()
        dismissAnimator = null
    }

    fun dismiss(runAnimation: Boolean = true) {
        if (!isActive) {
            return
        }
        isActive = false

        clearAnimator()

        if (runAnimation) {
            dismissAnimator = animate()
                .setDuration(DEFAULT_DISMISS_DURATION)
                .setListener(object: SimpleAnimatorListener() {
                    override fun onAnimationEnd(animator: Animator) {
                        release()
                    }
                    override fun onAnimationCancel(animator: Animator) {
                        release()
                    }
                })
                .setUpdateListener {
                    binding.root.alpha = 1f - it.animatedFraction
                }.also {
                    it.start()
                }
        } else {
            release()
        }
    }

    open class Builder(protected val context: Context) {

        var lifecycleOwner: LifecycleOwner? = null

        var text: String = ""

        @StringRes
        var textResId: Int = 0

        var state: BeepSnackBarState = BeepSnackBarState.INFO

        var action: BeepSnackBarAction = BeepSnackBarAction.None

        var enableSwipeDismiss: Boolean = true

        var duration: Int = Snackbar.LENGTH_LONG

        var rootView: ViewGroup? = null

        var onSnackBarDismissListener: OnSnackBarDismissListener? = null

        fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply {
            this.lifecycleOwner = lifecycleOwner
        }

        fun setText(text: String) = apply {
            this.text = text
            this.textResId = 0
        }

        fun setTextResId(@StringRes textResId: Int) = apply {
            this.text = ""
            this.textResId = textResId
        }

        fun setState(state: BeepSnackBarState) = apply {
            this.state = state
        }

        // SnackBar 의 상태를 초기화 할때 사용
        fun info() = apply {
            setText("")
            setState(BeepSnackBarState.INFO)
            setAction(BeepSnackBarAction.None)
        }

        // SnackBar 의 상태를 초기화 할때 사용
        fun error() = apply {
            setText("")
            setState(BeepSnackBarState.ERROR)
            setAction(BeepSnackBarAction.None)
        }

        fun setAction(action: BeepSnackBarAction) = apply {
            this.action = action
        }

        fun setEnableSwipeDismiss(enableSwipeDismiss: Boolean) = apply {
            this.enableSwipeDismiss = enableSwipeDismiss
        }

        fun setDuration(duration: Int) = apply {
            this.duration = duration
        }

        fun setRootView(rootView: ViewGroup) = apply {
            this.rootView = rootView
        }

        fun setOnSnackBarDismissListener(listener: OnSnackBarDismissListener?) = apply {
            this.onSnackBarDismissListener = listener
        }

        @Px
        var marginLeft = 24.dp

        @Px
        var marginTop = 0

        @Px
        var marginRight = 24.dp

        @Px
        var marginBottom = 16.dp

        fun setMargin(
            @Px left: Int = 0,
            @Px top: Int = 0,
            @Px right: Int = 0,
            @Px bottom: Int = 0,
        ) = apply {
            marginLeft = left
            marginTop = top
            marginRight = right
            marginBottom = bottom
        }

        fun setMargin(
            @Px horizontal: Int = 0,
            @Px vertical: Int = 0,
        ) = apply {
            marginLeft = horizontal
            marginTop = vertical
            marginRight = horizontal
            marginBottom = vertical
        }

        fun setMargin(
            @Px size: Int = 0
        ) = apply {
            marginLeft = size
            marginTop = size
            marginRight = size
            marginBottom = size
        }

        private var snackBar: WeakReference<BeepSnackBar?> = WeakReference(null)

        fun build(): BeepSnackBar {
            snackBar.get()?.dismiss()
            return BeepSnackBar(context = context).also {
                snackBar = WeakReference(it)
            }
        }

        fun show() {
            val snackBar = snackBar.get() ?: build()
            if (!snackBar.isActive || !snackBar.isVisible || !snackBar.isAttachedToWindow) {
                snackBar.builder = this
                snackBar.show()
            }
        }

        fun dismiss() {
            snackBar.get()?.dismiss()
            snackBar = WeakReference(null)
        }
    }
}