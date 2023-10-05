package com.lighthouse.beep.library.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer

object BarcodeGenerator {
    fun generate(value: String, width: Int, height: Int): Bitmap {
        val bitMatrix = Code128Writer().encode(value, BarcodeFormat.CODE_128, width, height)

        val pixels = IntArray(width * height) { i ->
            val y = i / width
            val x = i % width
            if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            .apply {
                setPixels(pixels, 0, width, 0, 0, width, height)
            }
    }
}