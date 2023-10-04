package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.parser.ExpiredParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class ExpiredRecognizer {

    private val expiredParser = ExpiredParser()

    private val textRecognizer = TextRecognizer()

    suspend fun recognize(bitmap: Bitmap): Date = withContext(Dispatchers.IO) {
        val inputs = textRecognizer.recognize(bitmap)
        expiredParser.parseExpiredDate(inputs).expired
    }
}
