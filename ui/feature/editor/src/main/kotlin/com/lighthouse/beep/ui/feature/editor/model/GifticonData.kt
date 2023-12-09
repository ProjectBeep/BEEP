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
    val thumbnail: GifticonThumbnail = GifticonThumbnail.Default(originUri),
    val thumbnailCropData: GifticonCropData = GifticonCropData.None,
    val name: String = "",
    val nameCropData: GifticonCropData = GifticonCropData.None,
    val brand: String = "",
    val brandCropData: GifticonCropData = GifticonCropData.None,
    val barcode: String = "",
    val barcodeCropData: GifticonCropData = GifticonCropData.None,
    val expired: Date = EMPTY_DATE,
    val expiredCropData: GifticonCropData = GifticonCropData.None,
    val isCashCard: Boolean = false,
    val balance: String = "",
    val balanceCropData: GifticonCropData = GifticonCropData.None,
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
        thumbnail = croppedImage?.let {
            GifticonThumbnail.Crop(
                bitmap = it,
                rect = croppedRect.toRectF()
            )
        } ?: GifticonThumbnail.Default(originUri),
        thumbnailCropData = croppedImage?.let {
            GifticonCropData(
                rect = croppedRect.toRectF(),
            )
        } ?: GifticonCropData.None,
        name = name,
        brand = brandName,
        barcode = barcode,
        expired = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
    )
}