package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.parser.BarcodeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarcodeRecognizer {

    private val barcodeParser = BarcodeParser()

    private val textRecognizer = TextRecognizer()

    suspend fun recognize(bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val inputs = textRecognizer.recognize(bitmap)
        barcodeParser.parseBarcode(inputs)
    }
}
