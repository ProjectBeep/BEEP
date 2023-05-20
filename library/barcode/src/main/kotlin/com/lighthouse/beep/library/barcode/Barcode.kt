package com.lighthouse.beep.library.barcode

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.google.zxing.oned.Code39Writer
import com.google.zxing.oned.Code93Writer

object Barcode {

    fun fromValue(
        context: Context,
        value: String,
        format: BarcodeFormat = BarcodeFormat.CODE_128,
        width: Int = context.resources.getDimensionPixelSize(R.dimen.width_barcode),
        height: Int = context.resources.getDimensionPixelSize(R.dimen.height_barcode),

    ): Bitmap {
        val bitMatrix = when (format) {
            BarcodeFormat.CODE_128 -> Code128Writer()
            BarcodeFormat.CODE_93 -> Code93Writer()
            BarcodeFormat.CODE_39 -> Code39Writer()
            else -> throw IllegalArgumentException()
        }.encode(value, format, width, height)

        val barcodeColor = context.getColor(R.color.black)
        val backgroundColor = context.getColor(android.R.color.white)

        val pixels = IntArray(bitMatrix.width * bitMatrix.height) { i ->
            val y = i / bitMatrix.width
            val x = i % bitMatrix.width
            if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
        }

        return Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888)
            .apply {
                setPixels(pixels, 0, bitMatrix.width, 0, 0, bitMatrix.width, bitMatrix.height)
            }
    }
}
