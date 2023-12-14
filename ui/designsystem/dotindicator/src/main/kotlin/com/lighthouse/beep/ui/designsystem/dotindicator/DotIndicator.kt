package com.lighthouse.beep.ui.designsystem.dotindicator

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.theme.R

class DotIndicator(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private val DEFAULT_DOT_SIZE = 8.dp
        private val DEFAULT_DOT_GAP = 8.dp
    }

    private var dotSize = DEFAULT_DOT_SIZE
    private var dotCurrentSize = DEFAULT_DOT_SIZE
    private var dotGap = DEFAULT_DOT_GAP

    private var measuredDotSize = dotSize.toFloat()
    private var measuredDotCurrentSize = dotCurrentSize.toFloat()
    private var measuredDotGap = dotGap.toFloat()
    private var measuredDotRadius = measuredDotSize / 2

    private val selectedDotColor = context.getColor(R.color.beep_pink)
    private val unselectedDotColor = context.getColor(R.color.medium_pink)

    private val dotPaint = Paint().apply {
        isAntiAlias = true
        color = unselectedDotColor
    }

    private var dotRects = arrayOf<RectF>()

    private var leftPosition = 0

    private val leftRectF = RectF()
    private val leftDotPaint = Paint().apply {
        isAntiAlias = true
    }

    private val rightRectF = RectF()
    private val rightDotPaint = Paint().apply {
        isAntiAlias = true
    }

    private var adapterDataObserver: RecyclerView.AdapterDataObserver? = null
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val offset = recyclerView.computeHorizontalScrollOffset()
            val extent = recyclerView.computeHorizontalScrollExtent()

            leftPosition = offset / extent
            val pageOffset = offset % extent / extent.toFloat()
            val insetSize = -(measuredDotCurrentSize - measuredDotSize) / 2

            leftRectF.set(dotRects[leftPosition])
            leftRectF.inset(insetSize * (1f - pageOffset), 0f)
            leftDotPaint.color =
                ArgbEvaluator().evaluate(
                    (1f - pageOffset),
                    unselectedDotColor,
                    selectedDotColor
                ) as Int

            if (leftPosition + 1 < dotRects.size) {
                rightRectF.set(dotRects[leftPosition + 1])
                rightRectF.inset(insetSize * pageOffset, 0f)
                rightDotPaint.color =
                    ArgbEvaluator().evaluate(
                        pageOffset,
                        unselectedDotColor,
                        selectedDotColor
                    ) as Int
            }
            invalidate()
        }
    }

    private fun updateDotCount(dotCount: Int) {
        if (dotRects.size != dotCount) {
            dotRects = Array(dotCount) { RectF() }
            if (leftPosition >= dotCount - 1) {
                leftPosition = (dotCount - 1).coerceAtLeast(0)
            }
            requestLayout()
        }
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.doOnAttach {
            updateDotCount(recyclerView.adapter?.itemCount ?: 0)

            adapterDataObserver = object :
                RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    updateDotCount(recyclerView.adapter?.itemCount ?: 0)
                }
            }

            adapterDataObserver?.let { observer ->
                recyclerView.adapter?.registerAdapterDataObserver(observer)
            }

            recyclerView.addOnScrollListener(onScrollListener)
        }

        recyclerView.doOnDetach {
            adapterDataObserver?.let { observer ->
                recyclerView.adapter?.unregisterAdapterDataObserver(observer)
            }
            adapterDataObserver = null
            recyclerView.removeOnScrollListener(onScrollListener)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val dotCount = dotRects.size
        val baseSideSize = dotCurrentSize - dotSize
        val baseDotsWidth =
            if (dotCount > 0) baseSideSize + dotSize * dotCount + dotGap * (dotCount - 1) else 0
        val baseDotsHeight = if (dotCount > 0) dotSize else 0

        val widthSize = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)

            else -> baseDotsWidth
        }

        val heightSize = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            else -> baseDotsHeight
        }

        val widthBaseDotSize =
            if (widthSize >= baseDotsWidth) dotSize else dotSize * widthSize / baseDotsWidth
        val heightBaseDotSize =
            if (heightSize >= baseDotsHeight) dotSize else heightSize
        measuredDotSize = widthBaseDotSize.coerceAtLeast(heightBaseDotSize).toFloat()
        val measuredScale = measuredDotSize / dotSize
        measuredDotRadius = measuredDotSize / 2

        measuredDotCurrentSize =
            if (widthSize >= baseDotsWidth) dotCurrentSize.toFloat() else (dotCurrentSize * measuredScale)
        measuredDotGap = dotGap * measuredScale

        val horizontalMargin =
            if (widthSize >= baseDotsWidth * measuredScale) widthSize - baseDotsWidth * measuredScale else 0f
        val verticalMargin =
            if (heightSize >= baseDotsHeight * measuredScale) heightSize - baseDotsHeight * measuredScale else 0f

        dotRects.forEachIndexed { i, rect ->
            rect.left =
                if (i == 0) horizontalMargin + (measuredDotCurrentSize - measuredDotSize) / 2
                else dotRects[i - 1].right + measuredDotGap
            rect.right = rect.left + measuredDotSize
            rect.top = verticalMargin
            rect.bottom = rect.top + measuredDotSize
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        dotRects.forEachIndexed { i, rectF ->
            when (i) {
                leftPosition ->
                    canvas.drawRoundRect(
                        leftRectF,
                        measuredDotRadius,
                        measuredDotRadius,
                        leftDotPaint
                    )

                leftPosition + 1 ->
                    canvas.drawRoundRect(
                        rightRectF,
                        measuredDotRadius,
                        measuredDotRadius,
                        rightDotPaint
                    )

                else -> canvas.drawRoundRect(rectF, measuredDotRadius, measuredDotRadius, dotPaint)
            }
        }
    }
}