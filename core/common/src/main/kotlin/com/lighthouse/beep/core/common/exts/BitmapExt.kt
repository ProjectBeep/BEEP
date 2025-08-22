package com.lighthouse.beep.core.common.exts

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect

fun Bitmap.rotated(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.calculateCenterCropRect(aspectRatio: Float): Rect {
    val bitmapAspectRatio = width.toFloat() / height
    return if (bitmapAspectRatio > aspectRatio) {
        val newWidth = (height * aspectRatio).toInt()
        val x = (width - newWidth) / 2
        Rect(x, 0, x + newWidth, height)
    } else {
        val newHeight = (width / aspectRatio).toInt()
        val y = (height - newHeight) / 2
        Rect(0, y, width, y + newHeight)
    }
}

fun Bitmap.centerCrop(aspectRatio: Float): Bitmap {
    val rect = calculateCenterCropRect(aspectRatio)
    return crop(rect)
}

fun Bitmap.crop(rect: Rect): Bitmap {
    return Bitmap.createBitmap(
        this,
        rect.left,
        rect.top,
        rect.width(),
        rect.height(),
    )
}
