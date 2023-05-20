package com.lighthouse.beep.data.mapper

import android.net.Uri
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult

internal fun GifticonRecognizeInfo.toGifticonRecognizerResult(
    originUri: Uri,
    croppedUri: Uri?,
): GifticonRecognizeResult {
    return GifticonRecognizeResult(
        name = name,
        brandName = brand,
        barcode = barcode,
        expiredAt = expiredAt,
        isCashCard = isCashCard,
        balance = balance,
        originUri = originUri,
        croppedUri = croppedUri,
        croppedRect = croppedRect,
    )
}
