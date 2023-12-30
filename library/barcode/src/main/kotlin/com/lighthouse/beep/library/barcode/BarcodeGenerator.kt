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

        val pixels = IntArray(width * height) { i ->
            val y = i / width
            val x = i % width
            if (bitMatrix.get(x, y)) Color.BLACK else backgroundColor
        }

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}