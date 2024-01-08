package com.lighthouse.beep.library.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BarcodeGenerator {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val barcodeCache = BarcodeLruCache(maxMemory / 8)

    suspend fun loadBarcode(
        value: String,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.WHITE,
    ): Bitmap {
        val barcodeKey = "${value}-${width}-${height}"
        val barcode = barcodeCache.get(barcodeKey)
        if (barcode != null) {
            return barcode
        }
        return generate(value, width, height, backgroundColor).also {
            barcodeCache.put(barcodeKey, it)
        }
    }

    private suspend fun generate(
        value: String,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.WHITE,
    ): Bitmap = withContext(Dispatchers.IO) {
        val bitMatrix = Code128Writer().encode(value, BarcodeFormat.CODE_128, width, height)
        var xScale = 1
        var yScale = 1
        while ((xScale + 1) * bitMatrix.width < width) {
            xScale += 1
        }
        while ((yScale + 1) * bitMatrix.height < height) {
            yScale += 1
        }

        val bitmap =
            Bitmap.createBitmap(bitMatrix.width * xScale, bitMatrix.height * yScale, Bitmap.Config.ARGB_8888)
        for (x in 0 until bitMatrix.width) {
            for (y in 0 until bitMatrix.height) {
                val color = if (bitMatrix.get(x, y)) Color.BLACK else backgroundColor
                for (xs in 0 until xScale) {
                    for (ys in 0 until yScale) {
                        bitmap.setPixel(x * xScale + xs, y * yScale + ys, color)
                    }
                }
            }
        }
        bitmap
    }
}