package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.RectF
import android.net.Uri
import androidx.core.graphics.toRectF
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import com.lighthouse.beep.core.common.exts.toFormattedString
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import com.lighthouse.beep.ui.dialog.textinput.TextInputFormat
import java.util.Date

internal data class GifticonData(
    val originUri: Uri,
    val thumbnailCropData: ThumbnailCropData = ThumbnailCropData.None,
    val name: String = "",
    val nameRect: RectF = EMPTY_RECT_F,
    val brand: String = "",
    val brandRect: RectF = EMPTY_RECT_F,
    val barcode: String = "",
    val barcodeRect: RectF = EMPTY_RECT_F,
    val expired: Date = EMPTY_DATE,
    val expiredRect: RectF = EMPTY_RECT_F,
    val isCashCard: Boolean = false,
    val balance: String = "",
    val balanceRect: RectF = EMPTY_RECT_F,
    val memo: String = "",
) {
    val displayBarcode: String = TextInputFormat.BARCODE.valueToTransformed(barcode)

    val displayExpired: String = if (expired == EMPTY_DATE) "" else expired.toFormattedString()

    val displayBalance: String = TextInputFormat.BALANCE.valueToTransformed(balance)

    val isInvalid
        get() = EditType.entries.any { it.isInvalid(this) }
}

internal fun GifticonRecognizeResult?.toGifticonData(originUri: Uri): GifticonData {
    this ?: return GifticonData(originUri)
    return GifticonData(
        originUri = originUri,
        thumbnailCropData = ThumbnailCropData(
            originWidth = imageWidth,
            originHeight = imageHeight,
            rect = croppedRect?.toRectF() ?: RectF(),
        ),
        name = name,
        brand = brandName,
        barcode = barcode,
        expired = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
    )
}