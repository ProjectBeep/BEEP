package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log

internal data class CropData(
    val originWidth: Int,
    val originHeight: Int,
    val cropRect: RectF,
) {
    val isCropped
        get() = cropRect != EMPTY_RECT_F

    fun calculateMatrix(viewRect: RectF): Matrix {
        val matrix = Matrix()
        if (isCropped) {
            Log.d("TEST", "crop : $cropRect")
            matrix.setRectToRect(cropRect, viewRect, Matrix.ScaleToFit.FILL)
        } else {
            val imageRect = RectF(0f, 0f, originWidth.toFloat(), originHeight.toFloat())
            val imageRatio = imageRect.width() / imageRect.height()
            val viewRatio = viewRect.width() / viewRect.height()

            if(imageRatio > viewRatio) {
                val newWidth = imageRect.height() * viewRatio
                imageRect.inset((imageRect.width() - newWidth) / 2, 0f)
            } else {
                val newHeight = imageRect.width() / viewRatio
                imageRect.inset(0f, (imageRect.height() - newHeight) / 2)
            }
            matrix.setRectToRect(imageRect, viewRect, Matrix.ScaleToFit.FILL)
        }
        return matrix
    }

    companion object {
        private val EMPTY_RECT_F = RectF()

        val None = CropData(0, 0, RectF())
    }
}