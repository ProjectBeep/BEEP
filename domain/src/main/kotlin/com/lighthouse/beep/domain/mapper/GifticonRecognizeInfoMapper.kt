package com.lighthouse.beep.domain.mapper

import android.net.Uri
import com.lighthouse.beep.core.common.exts.EMPTY_RECT
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult

internal fun GifticonRecognizeInfo.toGifticonRecognizerResult(
    originUri: Uri,
): GifticonRecognizeResult {
    return GifticonRecognizeResult(
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        name = name,
        brandName = brand,
        barcode = barcode,
        expiredAt = expiredAt,
        isCashCard = isCashCard,
        balance = balance,
        originUri = originUri,
        croppedImage = croppedImage,
        croppedRect = croppedRect ?: EMPTY_RECT,
    )
}
