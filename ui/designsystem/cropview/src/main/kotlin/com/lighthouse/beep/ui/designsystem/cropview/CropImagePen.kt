package com.lighthouse.beep.ui.designsystem.cropview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.view.MotionEvent
import com.lighthouse.beep.core.common.exts.dp
import kotlin.math.max
import kotlin.math.min
import com.lighthouse.beep.theme.R as ThemeR

internal class CropImagePen(
    private val view: CropImageView,
    private val listener: OnCropImagePenListener,
) {

    companion object {
        private val POINT_F_EMPTY = PointF()

        private val DEFAULT_PEN_SIZE = 36f.dp
        private val DEFAULT_PEN_POINTER_WIDTH = 1f.dp
    }

    private val penPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = DEFAULT_PEN_SIZE
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        alpha = 0xFF
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        color = Color.TRANSPARENT
    }

    private val penPointerPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = DEFAULT_PEN_POINTER_WIDTH
        style = Paint.Style.STROKE
        color = view.context.getColor(ThemeR.color.white)
    }

    private val drawPath = Path()
    private val drawRect = RectF()
    private val pointerPos = PointF()

    private val backgroundPaint by lazy {
        Paint().apply {
            color = view.context.getColor(ThemeR.color.black_60)
        }
    }

    fun onDraw(canvas: Canvas) {
        canvas.save()
        val layerId =
            canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), null)
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), backgroundPaint)
        canvas.drawPath(drawPath, penPaint)
        if (pointerPos != POINT_F_EMPTY) {
            canvas.drawCircle(
                pointerPos.x,
                pointerPos.y,
                penPaint.strokeWidth / 2f,
                penPointerPaint
            )
        }
        canvas.restoreToCount(layerId)
        canvas.restore()
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isActivePointer(event)) {
            return false
        }
        val x = event.x
        val y = event.y
        pointerPos.set(x, y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                listener.onPenTouchStart()
                drawPath.reset()
                drawPath.moveTo(x, y)
                drawRect.set(x, y, x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(x, y)
                calculateRect(x, y)
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
            -> {
                drawPath.lineTo(x, y)
                drawPath.close()
                pointerPos.set(POINT_F_EMPTY)

                calculateRect(x, y)
                val boundRect = view.boundRect
                drawRect.apply {
                    inset(-DEFAULT_PEN_SIZE / 2, -DEFAULT_PEN_SIZE / 2)
                    left = max(left, boundRect.left)
                    top = max(top, boundRect.top)
                    right = min(right, boundRect.right)
                    bottom = min(bottom, boundRect.bottom)
                }
                val minSize = view.minWindowSize
                val halfSize = minSize / 2
                val centerX = minOf(
                    maxOf(drawRect.centerX(), boundRect.left + halfSize),
                    boundRect.right - halfSize,
                )
                val centerY = minOf(
                    maxOf(drawRect.centerY(), boundRect.top + halfSize),
                    boundRect.bottom - halfSize,
                )
                if (drawRect.width() < minSize) {
                    drawRect.left = centerX - halfSize
                    drawRect.right = centerX + halfSize
                }
                if (drawRect.height() < minSize) {
                    drawRect.top = centerY - halfSize
                    drawRect.bottom = centerY + halfSize
                }

                activePointerId = null
                listener.onPenTouchComplete(drawRect)
                drawPath.reset()
            }
        }

        view.invalidate()
        return true
    }

    private fun calculateRect(x: Float, y: Float) {
        drawRect.apply {
            left = min(x, left)
            top = min(y, top)
            right = max(x, right)
            bottom = max(y, bottom)
        }
    }

    private var activePointerId: Int? = null

    private fun isActivePointer(event: MotionEvent): Boolean {
        val curPointerId = event.getPointerId(event.actionIndex)
        if (activePointerId == null) {
            activePointerId = curPointerId
        }
        return activePointerId == curPointerId
    }
}
