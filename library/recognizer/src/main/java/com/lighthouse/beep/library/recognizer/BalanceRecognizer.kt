package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.parser.BalanceParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BalanceRecognizer {

    private val balanceParser = BalanceParser()

    private val textRecognizer = TextRecognizer()

    suspend fun recognize(bitmap: Bitmap): Int = withContext(Dispatchers.IO) {
        val inputs = textRecognizer.recognize(bitmap)
        balanceParser.parseCashCard(inputs).balance
    }
}
