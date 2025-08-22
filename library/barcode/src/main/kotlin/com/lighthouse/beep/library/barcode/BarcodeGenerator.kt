package com.lighthouse.beep.library.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.EnumMap

object BarcodeGenerator {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val barcodeCache = BarcodeLruCache(maxMemory / 8)

    suspend fun loadBarcode(
        value: String,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.WHITE,
    ): Bitmap {
        val barcodeKey = "${value}-${width}-${height}-${backgroundColor}"
        val barcode = barcodeCache.get(barcodeKey)
        if (barcode != null) {
            return barcode
        }
        return generate(
            format = BarcodeFormat.CODE_128,
            value = value,
            width = width,
            height = height,
            backgroundColor = backgroundColor
        ).also {
            barcodeCache.put(barcodeKey, it)
        }
    }

    suspend fun loadQrCode(
        value: String,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.WHITE,
    ): Bitmap {
        val barcodeKey = "${value}-${width}-${height}-${backgroundColor}"
        val barcode = barcodeCache.get(barcodeKey)
        if (barcode != null) {
            return barcode
        }
        return generate(
            format = BarcodeFormat.QR_CODE,
            value = value,
            width = width,
            height = height,
            backgroundColor = backgroundColor
        ).also {
            barcodeCache.put(barcodeKey, it)
        }
    }

    private suspend fun generate(
        format: BarcodeFormat,
        value: String,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.WHITE,
    ): Bitmap = withContext(Dispatchers.IO) {
        val hints = mapOf(
            EncodeHintType.MARGIN to 0
        )
        val bitMatrix = when(format) {
            BarcodeFormat.CODE_128 -> Code128Writer().encode(value, format, width, height, hints)
            else -> {
                QRCodeWriter().encode(value, format, width, height, hints)
            }
        }
        var xScale = 1
        var yScale = 1
        while ((xScale + 1) * bitMatrix.width < width) {
            xScale += 1
        }
        while ((yScale + 1) * bitMatrix.height < height) {
            yScale += 1
        }

        val bitmap =
            Bitmap.createBitmap(
                bitMatrix.width * xScale,
                bitMatrix.height * yScale,
                Bitmap.Config.ARGB_8888
            )
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