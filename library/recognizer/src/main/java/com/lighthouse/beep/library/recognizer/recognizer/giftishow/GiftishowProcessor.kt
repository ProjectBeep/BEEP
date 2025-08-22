package com.lighthouse.beep.library.recognizer.recognizer.giftishow

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.processor.BaseProcessor
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessImage
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessText
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessTextTag

internal class GiftishowProcessor : BaseProcessor() {

    override fun processTextImage(bitmap: Bitmap): List<GifticonProcessText> {
        return listOf(
            cropAndScaleTextImage(
                GifticonProcessTextTag.GIFTICON_BRAND_NAME,
                bitmap,
                0.23f,
                0.83f,
                0.96f,
                0.94f,
            ),
        )
    }

    override fun processGifticonImage(origin: Bitmap, bitmap: Bitmap): GifticonProcessImage {
        val offsetX = (origin.width - bitmap.width) / 2
        val offsetY = origin.height - bitmap.height
        val image = cropGifticonImage(bitmap, 0.1f, 0.04f, 0.43f, 0.37f)
        return image.copy(
            rect = image.rect.apply {
                offset(offsetX, offsetY)
            },
        )
    }

    override fun preProcess(bitmap: Bitmap): Bitmap {
        val ratio = bitmap.width.toFloat() / bitmap.height
        return if (ratio > 1f) {
            val size = bitmap.height
            Bitmap.createBitmap(bitmap, (bitmap.width - size) / 2, 0, size, size)
        } else {
            val size = bitmap.width
            Bitmap.createBitmap(bitmap, 0, bitmap.height - size, size, size)
        }
    }
}
