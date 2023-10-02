package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.library.recognizer.parser.BalanceParser
import com.lighthouse.beep.library.recognizer.parser.BarcodeParser
import com.lighthouse.beep.library.recognizer.parser.ExpiredParser
import com.lighthouse.beep.library.recognizer.recognizer.TemplateRecognizer
import com.lighthouse.beep.library.recognizer.recognizer.giftishow.GiftishowRecognizer
import com.lighthouse.beep.library.recognizer.recognizer.kakao.KakaoRecognizer
import com.lighthouse.beep.library.recognizer.recognizer.smilecon.SmileConRecognizer
import com.lighthouse.beep.library.recognizer.recognizer.syrup.SyrupRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GifticonRecognizer {

    private val barcodeParser = BarcodeParser()
    private val expiredParser = ExpiredParser()
    private val balanceParser = BalanceParser()

    private val textRecognizer = TextRecognizer()

    private val templateRecognizerList = listOf(
        KakaoRecognizer(),
        SyrupRecognizer(),
        GiftishowRecognizer(),
        SmileConRecognizer(),
    )

    private fun getTemplateRecognizer(inputs: List<String>): TemplateRecognizer? {
        return templateRecognizerList.firstOrNull {
            it.match(inputs)
        }
    }

    suspend fun recognize(bitmap: Bitmap): GifticonRecognizeInfo = withContext(Dispatchers.IO) {
        val inputs = textRecognizer.recognize(bitmap)
        val barcodeResult = barcodeParser.parseBarcode(inputs)
        val expiredResult = expiredParser.parseExpiredDate(barcodeResult.filtered)
        var info = GifticonRecognizeInfo(
            imageWidth = bitmap.width,
            imageHeight = bitmap.height,
            candidate = expiredResult.filtered
        )
        getTemplateRecognizer(info.candidate)?.run {
            info = recognize(bitmap, info.candidate)
        }
        val balanceResult = balanceParser.parseCashCard(info.candidate)
        info.copy(
            barcode = barcodeResult.barcode,
            expiredAt = expiredResult.expired,
            isCashCard = balanceResult.balance > 0,
            balance = balanceResult.balance,
        )
    }
}
