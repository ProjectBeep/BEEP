package com.lighthouse.beep.library.recognizer.recognizer.kakao

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.processor.BaseProcessor
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessImage
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessText
import com.lighthouse.beep.library.recognizer.processor.GifticonProcessTextTag

internal class KakaoProcessor : BaseProcessor() {

    private val centerCropAspectRatio = 0.48f

    override fun processTextImage(bitmap: Bitmap): List<GifticonProcessText> {
        return listOf(
            cropAndScaleTextImage(
                GifticonProcessTextTag.BRAND_GIFTICON_NAME,
                bitmap,
                0f,
                0.4f,
                0.75f,
                0.55f,
            ),
        )
    }

    override fun preProcess(bitmap: Bitmap): Bitmap {
        val bitmapAspectRatio = bitmap.width.toFloat() / bitmap.height
        return if (bitmapAspectRatio > centerCropAspectRatio) {
            val newWidth = (bitmap.height * centerCropAspectRatio).toInt()
            Bitmap.createBitmap(bitmap, (bitmap.width - newWidth) / 2, 0, newWidth, bitmap.height)
        } else {
            val newHeight = (bitmap.width / centerCropAspectRatio).toInt()
            Bitmap.createBitmap(bitmap, 0, (bitmap.height - newHeight) / 2, bitmap.width, newHeight)
        }
    }

    override fun processGifticonImage(origin: Bitmap, bitmap: Bitmap): GifticonProcessImage {
        val offsetX = (origin.width - bitmap.width) / 2
        val offsetY = (origin.height - bitmap.height) / 2
        val image = cropGifticonImage(bitmap, 0.13125f, 0.05282f, 0.86875f, 0.41951f)
        return image.copy(
            rect = image.rect.apply {
                offset(offsetX, offsetY)
            },
        )
    }
}
