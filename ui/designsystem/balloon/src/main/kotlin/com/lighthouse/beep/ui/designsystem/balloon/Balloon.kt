package com.lighthouse.beep.ui.designsystem.balloon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.getViewPointOnScreen
import com.lighthouse.beep.ui.designsystem.balloon.databinding.BalloonLayoutBodyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.lighthouse.beep.theme.R as ThemeR
import java.lang.ref.WeakReference

@SuppressLint("ViewConstructor")
class Balloon private constructor(
    context: Context,
    private val builder: Builder,
) : ViewGroup(context) {

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    private lateinit var viewScope: CoroutineScope

    private val binding: BalloonLayoutBodyBinding =
        BalloonLayoutBodyBinding.inflate(LayoutInflater.from(context), this, true)

    private val handler = Handler(Looper.getMainLooper())

    private var onBalloonShowListener: OnBalloonShowListener? = null

    fun setOnBalloonShowListener(listener: OnBalloonShowListener?) {
        onBalloonShowListener = listener
    }

    private var onBalloonDismissListener: OnBalloonDismissListener? = null

    fun setOnBalloonDismissListener(listener: OnBalloonDismissListener?) {
        onBalloonDismissListener = listener
    }

    fun setOnBalloonClickListener(
        balloonClickListener: OnBalloonClickListener?,
        outsideClickListener: OnBalloonOutsideClickListener?
    ) {
        if (balloonClickListener != null || builder.dismissWhenClick) {
            binding.balloon.setOnClickListener {
                balloonClickListener?.onClick()
                if (builder.dismissWhenClick) {
                    dismiss()
                }
            }
        }

        setOnTouchListener(object : OnTouchListener {
            private val CLICK_DELAY = ViewConfiguration.getTapTimeout().toLong()
            private val cancelClickRunnable = Runnable {
                isClickable = false
            }

            private var touchTarget = BalloonTouchTarget.NONE
            private var isClickable = true

            private var activePointerId = INVALID_POINTER_ID

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, e: MotionEvent): Boolean {
                if (!isActivePointer(e)) {
                    return false
                }

                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchTarget = findTouchTarget(e)
                        handler.postDelayed(cancelClickRunnable, CLICK_DELAY)
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isClickable) {
                            when (touchTarget) {
                                BalloonTouchTarget.ANCHOR -> onInterceptAnchorClick()
                                BalloonTouchTarget.OUTSIDE -> onOutsideClick()
                                BalloonTouchTarget.NONE -> Unit
                            }
                        }
                        activePointerId = INVALID_POINTER_ID
                        touchTarget = BalloonTouchTarget.NONE
                        handler.removeCallbacks(cancelClickRunnable)
                        isClickable = true
                    }
                }
                return isTouchable(touchTarget)
            }

            private fun isActivePointer(event: MotionEvent): Boolean {
                val currentPointerId = event.getPointerId(event.actionIndex)
                if (activePointerId == INVALID_POINTER_ID) {
                    activePointerId = currentPointerId
                }
                return activePointerId == currentPointerId
            }

            private fun findTouchTarget(event: MotionEvent): BalloonTouchTarget {
                return if (builder.isInterceptAnchorClick &&
                    anchorRect.contains(event.x.toInt(), event.y.toInt())
                ) {
                    BalloonTouchTarget.ANCHOR
                } else {
                    BalloonTouchTarget.OUTSIDE
                }
            }

            private fun isTouchable(target: BalloonTouchTarget): Boolean {
                return target == BalloonTouchTarget.NONE ||
                        (builder.isInterceptAnchorClick && target == BalloonTouchTarget.ANCHOR) ||
                        (builder.dismissWhenClickOutside && target == BalloonTouchTarget.OUTSIDE)
            }

            private fun onInterceptAnchorClick() {
                balloonClickListener?.onClick()
                if (builder.dismissWhenClick) {
                    dismiss()
                }
            }

            private fun onOutsideClick() {
                if (outsideClickListener != null || builder.dismissWhenClickOutside) {
                    outsideClickListener?.onClick()
                    if (builder.dismissWhenClickOutside) {
                        dismiss()
                    }
                }
            }
        })
    }

    private val anchorPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
        private var prevPos: Point? = null

        override fun onPreDraw(): Boolean {
            val current = anchorView?.getViewPointOnScreen()
            if (prevPos != current) {
                prevPos = current
                doLayout(left, top, right, bottom)
            }
            return true
        }
    }

    init {
        observeLifecycle()
        initializeBackground()
        initializeBalloonRoot()
        initializeBalloonLayout()
        initializeBalloonArrow()
        initializeBalloonContent()
        initializeBalloonListeners()
    }

    private val balloonColor: Int
        get() = builder.balloonColorRes?.let { context.getColor(it) } ?: builder.balloonColor

    private fun observeLifecycle() {
        builder.lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dismiss()
                }
            }
        })
    }

    private fun initializeBackground() {
        with(binding.balloonCard) {
            clipToOutline = true
            background = GradientDrawable().apply {
                setColor(balloonColor)
                cornerRadius = builder.cornerRadius
            }
            setPadding(
                builder.paddingLeft,
                builder.paddingTop,
                builder.paddingRight,
                builder.paddingBottom
            )
        }
    }

    private fun initializeBalloonRoot() {
        binding.balloonWrapper.updateLayoutParams<MarginLayoutParams> {
            setMargins(
                builder.marginLeft,
                builder.marginTop,
                builder.marginRight,
                builder.marginBottom
            )
        }
    }

    private fun initializeBalloonLayout() {
        val layout = builder.contentLayoutRes?.let {
            LayoutInflater.from(context).inflate(it, binding.balloonCard, false)
        } ?: builder.contentView ?: throw IllegalArgumentException("contentView 를 설정해 주세요")

        val parentView = layout.parent as? ViewGroup
        parentView?.removeView(layout)
        binding.balloonCard.removeAllViews()
        binding.balloonCard.addView(layout)
    }

    private fun initializeBalloonArrow() {
        with(binding.balloonArrow) {
            val arrowDrawable =
                builder.arrowRes?.let { AppCompatResources.getDrawable(context, it) }
                    ?: createArrowDrawable(context)
            setImageDrawable(arrowDrawable)

            layoutParams =
                FrameLayout.LayoutParams(builder.arrowWidth.toInt(), builder.arrowHeight.toInt())
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(balloonColor))
            rotation = when (builder.alignment) {
                BalloonAlignment.LEFT -> 90f + builder.arrowRotation
                BalloonAlignment.TOP -> 180f + builder.arrowRotation
                BalloonAlignment.RIGHT -> -90f + builder.arrowRotation
                BalloonAlignment.BOTTOM -> 0f + builder.arrowRotation
            }
            isVisible = builder.isVisibleArrow
        }
    }

    private fun createArrowDrawable(context: Context): BitmapDrawable {
        val width = builder.arrowWidth
        val height = builder.arrowHeight
        val radius = minOf(builder.arrowRadius, width / 2f, height)

        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val x1 = (radius - height) * (2 * radius - 3 * width) / (2 * radius + 6 * height)
        val y1 = radius
        val x2 = width / 2 - radius / 3
        val y2 = -radius / 3f

        val path = Path()
        path.moveTo(0f, height)
        path.lineTo(x1, y1)
        path.cubicTo(x2, y2, width - x2, y2, width - x1, y1)
        path.lineTo(width, height)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            Color.BLACK
        }

        canvas.drawPath(path, paint)

        return BitmapDrawable(context.resources, bitmap)
    }

    private fun initializeBalloonContent() {
        val paddingSize = if (builder.isVisibleArrow) builder.arrowHeight.toInt() else 0
        with(binding.balloonContent) {
            when (builder.alignment) {
                BalloonAlignment.LEFT -> setPadding(0, 0, paddingSize, 0)
                BalloonAlignment.RIGHT -> setPadding(paddingSize, 0, 0, 0)
                BalloonAlignment.TOP -> setPadding(0, 0, 0, paddingSize)
                BalloonAlignment.BOTTOM -> setPadding(0, paddingSize, 0, 0)
            }
        }
    }

    private fun initializeBalloonListeners() {
        setOnBalloonShowListener(builder.onBalloonShowListener)
        setOnBalloonDismissListener(builder.onBalloonDismissListener)
        setOnBalloonClickListener(
            balloonClickListener = builder.onBalloonClickListener,
            outsideClickListener = builder.onBalloonOutsideClickListener,
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val child = getChildAt(0)
        measureChild(child, widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            doLayout(l, t, r, b)
        }
    }

    private val anchorRect = Rect()

    private fun getRootContainerRect(): Rect {
        val rootView = findRootView() ?: return Rect()
        val rootRect = Rect()
        rootView.getGlobalVisibleRect(rootRect)
        val leftView = builder.leftConstraintTargetView
        if (leftView != null) {
            rootView.left = when (builder.leftConstraintTargetDirection) {
                BalloonHorizontalDirection.LEFT -> leftView.left
                BalloonHorizontalDirection.RIGHT -> leftView.right
            }
        }
        val topView = builder.topConstraintTargetView
        if (topView != null) {
            rootView.top = when (builder.topConstraintTargetDirection) {
                BalloonVerticalDirection.TOP -> topView.top
                BalloonVerticalDirection.BOTTOM -> topView.bottom
            }
        }
        val rightView = builder.rightConstraintTargetView
        if (rightView != null) {
            rootView.right = when (builder.rightConstraintTargetDirection) {
                BalloonHorizontalDirection.LEFT -> rightView.left
                BalloonHorizontalDirection.RIGHT -> rightView.right
            }
        }
        val bottomView = builder.bottomConstraintTargetView
        if (bottomView != null) {
            rootView.bottom = when (builder.bottomConstraintTargetDirection) {
                BalloonVerticalDirection.TOP -> bottomView.left
                BalloonVerticalDirection.BOTTOM -> bottomView.right
            }
        }
        return rootRect
    }

    private fun doLayout(l: Int, t: Int, r: Int, b: Int) {
        val anchor = anchorView ?: return
        val child = getChildAt(0)
        val rootContainerRect = getRootContainerRect()
        anchor.getGlobalVisibleRect(anchorRect)

        val left = when (builder.alignment) {
            BalloonAlignment.TOP,
            BalloonAlignment.BOTTOM -> anchorRect.left - (child.measuredWidth - anchor.measuredWidth) / 2

            BalloonAlignment.LEFT -> anchorRect.left - child.measuredWidth
            BalloonAlignment.RIGHT -> anchorRect.left + anchor.measuredWidth
        } - rootContainerRect.left

        val top = when (builder.alignment) {
            BalloonAlignment.TOP -> anchorRect.top - child.measuredHeight
            BalloonAlignment.BOTTOM -> anchorRect.top + anchor.measuredHeight
            BalloonAlignment.LEFT,
            BalloonAlignment.RIGHT -> anchorRect.top - (child.measuredHeight - anchor.measuredHeight) / 2
        } - rootContainerRect.top

        val right = left + child.measuredWidth
        val bottom = top + child.measuredHeight

        val centerX = (r - l) / 2
        val centerY = (b - t) / 2
        val anchorCenterX = anchorRect.left + anchor.measuredWidth / 2
        val anchorCenterY = anchorRect.top + anchor.measuredHeight / 2

        val offsetX = when (builder.alignment) {
            BalloonAlignment.TOP,
            BalloonAlignment.BOTTOM -> when {
                anchorCenterX < centerX && left < l -> l - left
                anchorCenterX > centerX && right > r -> r - right
                else -> 0
            }

            BalloonAlignment.LEFT -> when {
                left < l -> l - left
                else -> 0
            }

            BalloonAlignment.RIGHT -> when {
                right > r -> r - right
                else -> 0
            }
        }

        val offsetY = when (builder.alignment) {
            BalloonAlignment.TOP -> when {
                top < t -> t - top
                else -> 0
            }

            BalloonAlignment.BOTTOM -> when {
                bottom > b -> b - bottom
                else -> 0
            }

            BalloonAlignment.LEFT,
            BalloonAlignment.RIGHT -> when {
                anchorCenterY < centerY && top < t -> t - top
                anchorCenterY > centerY && bottom > b -> b - bottom
                else -> 0
            }
        }

        binding.balloonArrow.x = when (builder.alignment) {
            BalloonAlignment.LEFT -> {
                binding.balloonCard.x + binding.balloonCard.measuredWidth - (builder.arrowWidth - builder.arrowHeight) / 2
            }

            BalloonAlignment.RIGHT -> {
                binding.balloonCard.x - binding.balloonArrow.measuredWidth + (builder.arrowWidth - builder.arrowHeight) / 2
            }

            BalloonAlignment.TOP,
            BalloonAlignment.BOTTOM -> getArrowConstraintPositionX(
                anchorRect.left + anchor.measuredWidth / 2 - rootContainerRect.left,
                left + builder.marginLeft + offsetX,
                right - builder.marginRight + offsetX,
            )
        }
        binding.balloonArrow.y = when (builder.alignment) {
            BalloonAlignment.LEFT,
            BalloonAlignment.RIGHT -> getArrowConstraintPositionY(
                anchorRect.top + anchor.measuredHeight / 2 - rootContainerRect.top,
                top + builder.marginTop + offsetY,
                bottom - builder.marginBottom + offsetY,
            )

            BalloonAlignment.TOP -> binding.balloonCard.y + binding.balloonCard.measuredHeight
            BalloonAlignment.BOTTOM -> binding.balloonCard.y - binding.balloonArrow.measuredHeight
        }

        child.layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY)
    }

    private fun getArrowConstraintPositionX(anchorCenterX: Int, left: Int, right: Int): Float {
        val minPosition = left + builder.cornerRadius
        val maxPosition = right - builder.cornerRadius - builder.arrowWidth
        val arrowPosition = anchorCenterX - builder.arrowWidth / 2f
        return if (minPosition < maxPosition) {
            arrowPosition.coerceIn(minPosition, maxPosition) - left
        } else {
            (right - left) / 2f
        }
    }

    private fun getArrowConstraintPositionY(anchorCenterY: Int, top: Int, bottom: Int): Float {
        val minPosition = top + builder.cornerRadius
        val maxPosition = bottom - builder.cornerRadius - builder.arrowHeight
        val arrowPosition = anchorCenterY - builder.arrowHeight / 2f
        return if (minPosition < maxPosition) {
            arrowPosition - top
        } else {
            (bottom - top) / 2f - builder.arrowHeight / 2f
        }
    }

    private var anchorView: View? = null

    private fun findRootView(): ViewGroup? {
        builder.rootView?.let {
            return it
        }

        val activity = context as? Activity
        return activity?.window?.decorView as? ViewGroup
    }

    fun show(anchor: View) {
        dismiss()
        val rootView = findRootView() ?: return
        val parent = parent as? ViewGroup
        parent?.removeView(this)
        rootView.addView(this, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        doOnAttach {
            anchorView = anchor.also {
                it.viewTreeObserver.addOnPreDrawListener(anchorPreDrawListener)
            }

            doOnDetach {
                anchor.viewTreeObserver.removeOnPreDrawListener(anchorPreDrawListener)
            }

            if (builder.autoDismissDelay > 0L) {
                viewScope.launch {
                    delay(builder.autoDismissDelay)
                    dismiss()
                }
            }
            onBalloonShowListener?.onShow()
        }
    }

    fun dismiss() {
        removeView(builder.contentView)

        val parent = parent as? ViewGroup
        parent?.removeView(this)

        onBalloonDismissListener?.onDismiss()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        viewScope = CoroutineScope(Dispatchers.Main)
    }

    override fun onDetachedFromWindow() {
        viewScope.cancel()

        super.onDetachedFromWindow()
    }

    @Suppress("UNUSED")
    open class Builder(protected val context: Context) {

        var lifecycleOwner: LifecycleOwner? = null

        var isVisibleArrow = true

        var isInterceptAnchorClick = true

        @DrawableRes
        var arrowRes: Int? = R.drawable.tooltip_arrow

        var arrowRotation: Int = 180

        @Px
        var arrowWidth: Float = 12f.dp

        @Px
        var arrowHeight: Float = 6f.dp

        @Px
        var arrowRadius: Float = 0f

        var alignment = BalloonAlignment.TOP

        @ColorInt
        var balloonColor = Color.WHITE

        @ColorRes
        var balloonColorRes: Int? = ThemeR.color.beep_pink

        @Px
        var paddingLeft = 18.dp

        @Px
        var paddingTop = 10.dp

        @Px
        var paddingRight = 18.dp

        @Px
        var paddingBottom = 10.dp

        @Px
        var marginLeft = 0

        @Px
        var marginTop = 0

        @Px
        var marginRight = 0

        @Px
        var marginBottom = 0

        var leftConstraintTargetView: View? = null
        var leftConstraintTargetDirection: BalloonHorizontalDirection = BalloonHorizontalDirection.RIGHT

        var topConstraintTargetView: View? = null
        var topConstraintTargetDirection: BalloonVerticalDirection = BalloonVerticalDirection.BOTTOM

        var rightConstraintTargetView: View? = null
        var rightConstraintTargetDirection: BalloonHorizontalDirection = BalloonHorizontalDirection.LEFT

        var bottomConstraintTargetView: View? = null
        var bottomConstraintTargetDirection: BalloonVerticalDirection = BalloonVerticalDirection.TOP

        @Px
        var cornerRadius: Float = 17.5f.dp

        var contentView: View? = null

        @LayoutRes
        var contentLayoutRes: Int? = null

        var rootView: ViewGroup? = null

        var dismissWhenClick: Boolean = true

        var dismissWhenClickOutside: Boolean = true

        var autoDismissDelay: Long = 0L

        var onBalloonClickListener: OnBalloonClickListener? = null

        var onBalloonDismissListener: OnBalloonDismissListener? = null

        var onBalloonOutsideClickListener: OnBalloonOutsideClickListener? = null

        var onBalloonShowListener: OnBalloonShowListener? = null

        fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply {
            this.lifecycleOwner = lifecycleOwner
        }

        fun setIsVisibleArrow(value: Boolean) = apply {
            isVisibleArrow = value
        }

        fun setIsInterceptAnchorClick(value: Boolean) = apply {
            isInterceptAnchorClick = value
        }

        fun setArrowRes(@DrawableRes resId: Int, rotation: Int = 0) = apply {
            arrowRes = resId
            arrowRotation = rotation
        }

        fun setArrowSize(@Px width: Float, height: Float) = apply {
            arrowWidth = width
            arrowHeight = height
        }

        fun setArrowSize(@Px size: Float) = apply {
            setArrowSize(size, size)
        }

        fun setArrowRadius(@Px value: Float) = apply {
            arrowRadius = value
        }

        fun setAlignment(value: BalloonAlignment) = apply {
            alignment = value
        }

        fun setBalloonColor(@ColorInt value: Int) = apply {
            balloonColor = value
        }

        fun setBalloonColorRes(@ColorRes redId: Int) = apply {
            balloonColorRes = redId
        }

        fun setPadding(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) = apply {
            paddingLeft = left
            paddingTop = top
            paddingRight = right
            paddingBottom = bottom
        }

        fun setPadding(@Px horizontal: Int, @Px vertical: Int) = apply {
            setPadding(horizontal, vertical, horizontal, vertical)
        }

        fun setPadding(@Px value: Int) = apply {
            setPadding(value, value, value, value)
        }

        fun setMargin(
            @Px left: Int = 0,
            @Px top: Int = 0,
            @Px right: Int = 0,
            @Px bottom: Int = 0
        ) = apply {
            marginLeft = left
            marginTop = top
            marginRight = right
            marginBottom = bottom
        }

        fun setMargin(@Px horizontal: Int = 0, @Px vertical: Int = 0) = apply {
            setMargin(horizontal, vertical, horizontal, vertical)
        }

        fun setMargin(@Px value: Int) = apply {
            setMargin(value, value, value, value)
        }

        fun setLeftConstraint(target: View, direction: BalloonHorizontalDirection = BalloonHorizontalDirection.LEFT) {
            leftConstraintTargetView = target
            leftConstraintTargetDirection = direction
        }

        fun setTopConstraint(target: View, direction: BalloonVerticalDirection = BalloonVerticalDirection.BOTTOM) {
            topConstraintTargetView = target
            topConstraintTargetDirection = direction
        }

        fun setRightConstraint(target: View, direction: BalloonHorizontalDirection = BalloonHorizontalDirection.RIGHT) {
            rightConstraintTargetView = target
            rightConstraintTargetDirection = direction
        }

        fun setBottomConstraint(target: View, direction: BalloonVerticalDirection = BalloonVerticalDirection.TOP) {
            bottomConstraintTargetView = target
            bottomConstraintTargetDirection = direction
        }

        fun setCornerRadius(@Px value: Float) = apply {
            cornerRadius = value
        }

        fun setRootView(viewGroup: ViewGroup) = apply {
            this.rootView = viewGroup
        }

        fun setContentView(value: View) = apply {
            contentView = value
        }

        fun setContentLayoutRes(@LayoutRes value: Int) = apply {
            contentLayoutRes = value
        }

        fun setDismissWhenClick(value: Boolean) = apply {
            dismissWhenClick = value
        }

        fun setDismissWhenClickOutside(value: Boolean) = apply {
            dismissWhenClickOutside = value
        }

        fun setAutoDismissDelay(value: Long) = apply {
            autoDismissDelay = value
        }

        fun setOnBalloonClickListener(listener: OnBalloonClickListener) = apply {
            onBalloonClickListener = listener
        }

        fun setOnBalloonDismissListener(listener: OnBalloonDismissListener) = apply {
            onBalloonDismissListener = listener
        }

        fun setOnBalloonOutsideClickListener(listener: OnBalloonOutsideClickListener) = apply {
            onBalloonOutsideClickListener = listener
        }

        fun setOnBalloonShowListener(listener: OnBalloonShowListener) = apply {
            onBalloonShowListener = listener
        }

        private var balloon: WeakReference<Balloon?> = WeakReference(null)

        open fun onPreBuild(dismissBalloon: () -> Unit) = apply { }

        fun build(): Balloon {
            onPreBuild {
                balloon.get()?.dismiss()
            }
            return Balloon(context = context, builder = this).also {
                balloon = WeakReference(it)
            }
        }

        fun show(anchor: View) {
            val balloon = balloon.get() ?: build()
            if (!balloon.isVisible || !balloon.isAttachedToWindow) {
                balloon.show(anchor)
            }
        }

        fun dismiss() {
            balloon.get()?.dismiss()
            balloon = WeakReference(null)
        }
    }
}