package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.lighthouse.beep.library.recognizer.parser.BarcodeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class BarcodeRecognizer {

    private val barcodeParser = BarcodeParser()

    private val textRecognizer = TextRecognizer()

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_CODE_128)
        .build()

    private val client = BarcodeScanning.getClient(options)

    suspend fun recognize(
        bitmap: Bitmap,
        scanMode: BarcodeScanMode = BarcodeScanMode.BOTH,
    ): String = withContext(Dispatchers.IO) {
        var barcode = ""
        if (scanMode == BarcodeScanMode.BOTH || scanMode == BarcodeScanMode.IMAGE) {
            barcode = recognizeImage(bitmap)
        }
        if (barcode.isEmpty() &&
            (scanMode == BarcodeScanMode.BOTH || scanMode == BarcodeScanMode.TEXT)
        ) {
            barcode = recognizeText(bitmap)
        }
        barcode
    }

    private suspend fun recognizeImage(bitmap: Bitmap): String {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            client.process(image).addOnCompleteListener {
                val barcode = when {
                    it.isSuccessful -> it.result.firstOrNull()?.rawValue ?: ""
                    else -> ""
                }
                continuation.resume(barcode)
            }
        }
    }

    private suspend fun recognizeText(bitmap: Bitmap): String {
        val inputs = textRecognizer.recognize(bitmap)
        return barcodeParser.parseBarcode(inputs).barcode
    }
}

enum class BarcodeScanMode {
    BOTH,
    IMAGE,
    TEXT,
}
