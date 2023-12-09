package com.lighthouse.beep.ui.designsystem.cropview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.math.max
import kotlin.math.min

class CropImageView(
    context: Context,
    attrs: AttributeSet?,
) : View(context, attrs) {

    private lateinit var viewScope: CoroutineScope

    private var originBitmap: Bitmap? = null

    // 실제 이미지의 크기에 맞는 Rect
    private val realImageRect = RectF()

    // 현재 화면에 그려지고 있는 Rect, Matrix
    private val curImageRect = RectF()

    val boundLeft: Float
        get() = max(curImageRect.left, 0f)

    val boundTop: Float
        get() = max(curImageRect.top, 0f)

    val boundRight: Float
        get() = min(curImageRect.right, width.toFloat())

    val boundBottom: Float
        get() = min(curImageRect.bottom, height.toFloat())

    val boundRect
        get() = RectF(boundLeft, boundTop, boundRight, boundBottom)

    private val mainMatrix = Matrix()
    private val mainInverseMatrix = Matrix()

    var zoom = 1f
        private set

    var cropImageMode = CropImageMode.DRAG_WINDOW
        private set

    private fun setCropImageMode(mode: CropImageMode) {
        cropImageMode = mode
        onCropImageModeChangeListener?.onChange(mode)
    }

    private var onCropImageModeChangeListener: OnCropImageModeChangeListener? = null

    fun setOnCropImageModeChangeListener(listener: OnCropImageModeChangeListener?) {
        onCropImageModeChangeListener = listener
    }

    private fun calculateRealCropRect(bitmap: Bitmap?, viewCropRect: RectF) = RectF(viewCropRect).apply {
        mainMatrix.invert(mainInverseMatrix)
        mainInverseMatrix.mapRect(this)
//        if (bitmap != null) {
//            if (left < 0f) {
//                left = 0f
//            }
//            if (top < 0f) {
//                top = 0f
//            }
//            if (right > bitmap.width) {
//                right = bitmap.width.toFloat()
//            }
//            if (bottom > bitmap.height) {
//                bottom = bitmap.height.toFloat()
//            }
//        }
    }

    fun setCropInfo(cropRect: RectF, zoom: Float) {
        val curCropRect = calculateRealCropRect(originBitmap, cropImageWindow.curCropRect)
        if (cropRect == curCropRect && cropRect != EMPTY_RECT_F && this.zoom == zoom) {
            return
        }
        cropImageWindow.initRect(originBitmap, cropRect)
        // aspectRatio 가 설정되 있으면 Pen 모드를 쓸 수 없다
        if (!enableAspectRatio && cropRect == EMPTY_RECT_F) {
            setCropImageMode(CropImageMode.DRAW_PEN)
            this.zoom = 1f
            calculateMatrix(RectF(realImageRect))
            invalidate()
        } else {
            setCropImageMode(CropImageMode.DRAG_WINDOW)
            if (zoom != 0f) {
                this.zoom = zoom
            }
            if (cropRect == EMPTY_RECT_F) {
                calculateMatrix(RectF(cropImageWindow.curCropRect))
            } else {
                calculateMatrix(RectF(cropRect))
            }

            if (zoom == 0f) {
                this.zoom = 1f
                applyZoom(cropImageWindow.curCropRect, false)
            }
            invalidate()
        }
    }

    private val onCropPenListener = object : OnCropImagePenListener {
        override fun onPenTouchComplete(viewCropRect: RectF) {
            val cropRect = calculateRealCropRect(originBitmap, viewCropRect)
            setCropImageMode(CropImageMode.DRAG_WINDOW)
            calculateMatrixByViewCropRect(viewCropRect)
            applyZoom(viewCropRect, animate = true)
            val bitmap = originBitmap
            if (bitmap != null) {
                onChangeCropRectListener?.onChange(bitmap, cropRect, zoom)
            }
        }
    }

    private val cropImagePen = CropImagePen(this, onCropPenListener)

    private val onCropImageWindowListener = object : OnCropImageWindowListener {
        override fun onWindowMove(unconsumedX: Float, unconsumedY: Float, boundRect: RectF) {
            if (unconsumedX == 0f && unconsumedY == 0f) {
                return
            }

            val offsetX = when {
                boundRect.left + unconsumedX < curImageRect.left -> curImageRect.left - boundRect.left
                boundRect.right + unconsumedX > curImageRect.right -> curImageRect.right - boundRect.right
                else -> unconsumedX
            }

            val offsetY = when {
                boundRect.top + unconsumedY < curImageRect.top -> curImageRect.top - boundRect.top
                boundRect.bottom + unconsumedY > curImageRect.bottom -> curImageRect.bottom - boundRect.bottom
                else -> unconsumedY
            }

            if (offsetX != 0f || offsetY != 0f) {
                mainMatrix.postTranslate(-offsetX, -offsetY)
                mapCurrentImageRectByMatrix()
            }
        }

        override fun onWindowResized(viewCropRect: RectF) {
            applyZoom(viewCropRect, animate = true)
        }

        override fun onWindowTouchComplete(viewCropRect: RectF) {
            val bitmap = originBitmap
            if (bitmap != null) {
                val cropRect = calculateRealCropRect(bitmap, viewCropRect)
                onChangeCropRectListener?.onChange(bitmap, cropRect, zoom)
            }
        }
    }

    private val cropImageWindow = CropImageWindow(this, onCropImageWindowListener)

    // AspectRatio = Width / Height
    var enableAspectRatio = true
    var aspectRatio = 1f

    private val cropZoomAnimation = object : Animation() {
        private val startCropRect = RectF()
        private val endCropRect = RectF()

        private val startImageRect = RectF()
        private val endImageRect = RectF()

        private val startMatrixPoints = FloatArray(9)
        private val endMatrixPoints = FloatArray(9)

        private val animMatrixPoints = FloatArray(9)

        init {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            cropImageWindow.curCropRect.set(
                startCropRect.left + (endCropRect.left - startCropRect.left) * interpolatedTime,
                startCropRect.top + (endCropRect.top - startCropRect.top) * interpolatedTime,
                startCropRect.right + (endCropRect.right - startCropRect.right) * interpolatedTime,
                startCropRect.bottom + (endCropRect.bottom - startCropRect.bottom) * interpolatedTime,
            )

            curImageRect.set(
                startImageRect.left + (endImageRect.left - startImageRect.left) * interpolatedTime,
                startImageRect.top + (endImageRect.top - startImageRect.top) * interpolatedTime,
                startImageRect.right + (endImageRect.right - startImageRect.right) * interpolatedTime,
                startImageRect.bottom + (endImageRect.bottom - startImageRect.bottom) * interpolatedTime,
            )

            for (i in animMatrixPoints.indices) {
                animMatrixPoints[i] =
                    startMatrixPoints[i] + (endMatrixPoints[i] - startMatrixPoints[i]) * interpolatedTime
            }
            mainMatrix.setValues(animMatrixPoints)
            invalidate()
        }

        fun setStartState(cropRect: RectF, imageRect: RectF, imageMatrix: Matrix) {
            startCropRect.set(cropRect)
            startImageRect.set(imageRect)
            imageMatrix.getValues(startMatrixPoints)
        }

        fun setEndState(cropRect: RectF, imageRectF: RectF, imageMatrix: Matrix) {
            endCropRect.set(cropRect)
            endImageRect.set(imageRectF)
            imageMatrix.getValues(endMatrixPoints)
        }
    }

    private var onChangeCropRectListener: OnChangeCropRectListener? = null
    fun setOnChangeCropRectListener(onChangeCropRectListener: OnChangeCropRectListener?) {
        this.onChangeCropRectListener = onChangeCropRectListener
    }

    fun setBitmap(bitmap: Bitmap?) {
        bitmap ?: return
        originBitmap = bitmap
        realImageRect.set(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        curImageRect.set(realImageRect)
        setCropInfo(EMPTY_RECT_F, 1f)
    }

    private fun applyZoom(
        viewCropRect: RectF,
        animate: Boolean = true,
    ) {
        Log.d("TEST", "applyZoom : ${curImageRect} ${realImageRect}")
        if (width == 0 || height == 0 || originBitmap == null) {
            return
        }

        val cropWidth = viewCropRect.width()
        val cropHeight = viewCropRect.height()

        var newZoom = zoom
        /*
         * CropWindow 의 가로와 세로가 화면의 50% 이하의 크기가 된다면,
         * CropWindow 은 (1 <= a < 2) 의 값을 곱해도 화면 보다 크지 않기 때문에 scale 이 1 이상이 된다.
         * 1.5 를 곱해 주는 이유는 한번에 너무 많이 커지는 것이 부담 스럽기 때문이다
         */
        if (zoom < MAX_ZOOM && cropWidth < width * 0.5f && cropHeight < height * 0.5f) {
            val scaleW = width / cropWidth * 0.66f * zoom
            val scaleH = height / cropHeight * 0.66f * zoom
            newZoom = minOf(scaleW, scaleH, MAX_ZOOM)
        }
        /*
         * CropWindow 의 가로와 세로가 화면의 66% 보다 커진다면,
         * CropWindow 은 (a > 1.5) 을 곱한다면 scale 이 1 이하가 되게 된다.
         * 2 를 곱하 면서 천천히 Zoom을 감소 시킨다
         */
        else if (zoom > MIN_ZOOM && cropWidth > width * 0.66f || cropHeight > height * 0.66f) {
            val scaleW = width / cropWidth * 0.5f * zoom
            val scaleH = height / cropHeight * 0.5f * zoom
            newZoom = max(min(scaleW, scaleH), MIN_ZOOM)
        }

        if(animate) {
            if (newZoom != zoom) {
                cropZoomAnimation.setStartState(cropImageWindow.curCropRect, curImageRect, mainMatrix)
                zoom = newZoom
                applyMatrix(viewCropRect, animate = true)
            }
        } else {
            zoom = newZoom
            applyMatrix(viewCropRect, animate = false)
        }
    }

    /**
     * 화면이 갑자기 많이 바뀌는 상황에서 적절하게 위치를 조정하기 위한 함수
     * ex) onLayout, applyZoom
     */
    private fun applyMatrix(
        viewCropRect: RectF,
        animate: Boolean = true,
    ) {
        Log.d("TEST", "applyMatrix : ${curImageRect} ${realImageRect}")
        val bitmap = originBitmap
        if (width == 0 || height == 0 || bitmap == null) {
            return
        }

        calculateMatrixByViewCropRect(viewCropRect)

        if (animate) {
            cropZoomAnimation.setEndState(cropImageWindow.curCropRect, curImageRect, mainMatrix)
            startAnimation(cropZoomAnimation)
        } else {
            invalidate()
        }
    }

    // 실제 이미지의 cropRect 를 View 에 맞춰서 변환 하고 mainMatrix 도 다시 계산
    private fun calculateMatrixByViewCropRect(viewCropRect: RectF) {
        calculateMatrix(calculateRealCropRect(originBitmap, viewCropRect))
    }

    private fun calculateMatrix(cropRect: RectF) {
        val bitmap = originBitmap
        if (width == 0 || height == 0 || bitmap == null) {
            return
        }

        // 1. mainMatrix 를 초기화
        mainMatrix.reset()

        // 2. 이미지를 화면에 맞게 키운다
        val wScale = width / bitmap.width.toFloat()
        val hScale = height / bitmap.height.toFloat()
        val scale = min(wScale, hScale) * zoom
        mainMatrix.postScale(scale, scale)
        mapCurrentImageRectByMatrix()

        // 3. 변경된 이미지를 화면의 가운데로 이동
        val offsetX = (width - curImageRect.width()) / 2
        val offsetY = (height - curImageRect.height()) / 2
        mainMatrix.postTranslate(offsetX, offsetY)
        mainMatrix.mapRect(cropRect)
        mapCurrentImageRectByMatrix()

        // 4. ZoomOffset 구하기
        val zoomOffsetX = when {
            width > curImageRect.width() -> 0f
            else -> max(
                min(width / 2 - cropRect.centerX(), -curImageRect.left),
                width - curImageRect.right,
            )
        }
        val zoomOffsetY = when {
            height > curImageRect.height() -> 0f
            else -> max(
                min(height / 2 - cropRect.centerY(), -curImageRect.top),
                height - curImageRect.bottom,
            )
        }

        mainMatrix.postTranslate(zoomOffsetX, zoomOffsetY)
        cropRect.offset(zoomOffsetX, zoomOffsetY)
        mapCurrentImageRectByMatrix()

        cropImageWindow.curCropRect.set(cropRect)
    }

    // 현재 계산된 Matrix 를 이용 하여 curImageRect 를 계산 해준다
    private fun mapCurrentImageRectByMatrix() {
        curImageRect.set(realImageRect)
        mainMatrix.mapRect(curImageRect)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            applyZoom(cropImageWindow.curCropRect, false)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap = originBitmap
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, mainMatrix, null)
            when (cropImageMode) {
                CropImageMode.DRAW_PEN -> cropImagePen.onDraw(canvas)
                CropImageMode.DRAG_WINDOW -> cropImageWindow.onDraw(canvas)
                CropImageMode.NONE -> Unit
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (cropImageMode) {
            CropImageMode.DRAW_PEN -> cropImagePen.onTouchEvent(event)
            CropImageMode.DRAG_WINDOW -> cropImageWindow.onTouchEvent(event)
            CropImageMode.NONE -> false
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        viewScope = CoroutineScope(Dispatchers.Main.immediate)
    }

    override fun onDetachedFromWindow() {
        viewScope.cancel()

        super.onDetachedFromWindow()
    }

    companion object {
        private const val MIN_ZOOM = 1f
        private const val MAX_ZOOM = 2f
    }
}
