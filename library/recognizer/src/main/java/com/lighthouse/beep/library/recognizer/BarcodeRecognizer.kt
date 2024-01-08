package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.lighthouse.beep.library.recognizer.model.BarcodeInfo
import com.lighthouse.beep.library.recognizer.parser.BarcodeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class BarcodeRecognizer {

    private val barcodeParser = BarcodeParser()

    private val textRecognizer = TextRecognizer()

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_PDF417,
        ).build()

    private val client = BarcodeScanning.getClient(options)

    suspend fun recognize(
        bitmap: Bitmap,
        scanMode: BarcodeScanMode = BarcodeScanMode.BOTH,
    ): BarcodeInfo = withContext(Dispatchers.IO) {
        var info = BarcodeInfo.None
        if (scanMode == BarcodeScanMode.BOTH || scanMode == BarcodeScanMode.IMAGE) {
            info = recognizeImage(bitmap)
        }
        if (info.isNone &&
            (scanMode == BarcodeScanMode.BOTH || scanMode == BarcodeScanMode.TEXT)
        ) {
            info = recognizeText(bitmap)
        }
        info
    }

    private suspend fun recognizeImage(bitmap: Bitmap): BarcodeInfo {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            client.process(image).addOnCompleteListener {
                val result = it.result.firstOrNull()
                val info = BarcodeInfo(
                    type = result?.valueType ?: Barcode.FORMAT_CODE_128,
                    barcode = result?.rawValue ?: "",
                )
                continuation.resume(info)
            }
        }
    }

    private suspend fun recognizeText(bitmap: Bitmap): BarcodeInfo {
        val inputs = textRecognizer.recognize(bitmap)
        val result = barcodeParser.parseBarcode(inputs)
        return BarcodeInfo(
            type = result.type,
            barcode = result.barcode
        )
    }
}

enum class BarcodeScanMode {
    BOTH,
    IMAGE,
    TEXT,
}
