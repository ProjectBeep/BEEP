package com.lighthouse.beep.ui.feature.editor.model

import android.net.Uri
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.ofDate
import com.lighthouse.beep.core.common.exts.ofMonth
import com.lighthouse.beep.core.common.exts.ofYear
import com.lighthouse.beep.core.common.exts.toFormattedString
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.ui.dialog.textinput.TextInputFormat
import java.util.Date

internal data class GifticonData(
    val originUri: Uri,
    val imagePath: String,
    val thumbnail: EditGifticonThumbnail = EditGifticonThumbnail.Default(originUri),
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

    val expiredYear: String = if (expired == EMPTY_DATE) "" else expired.ofYear().toString()

    val expiredMonth: String =
        if (expired == EMPTY_DATE) "" else String.format("%02d", expired.ofMonth())

    val expiredDate: String =
        if (expired == EMPTY_DATE) "" else String.format("%02d", expired.ofDate())

    val isInvalid
        get() = EditType.entries.any { it.isInvalid(this) }
}

internal fun GifticonRecognizeInfo?.toGifticonData(
    originUri: Uri,
    imagePath: String,
): GifticonData {
    this ?: return GifticonData(
        originUri = originUri,
        imagePath = imagePath,
    )
    return GifticonData(
        originUri = originUri,
        imagePath = imagePath,
        thumbnail = croppedImage?.let {
            EditGifticonThumbnail.Crop(
                bitmap = it,
                rect = croppedRect
            )
        } ?: EditGifticonThumbnail.Default(originUri),
        thumbnailCropData = croppedImage?.let {
            GifticonCropData(
                bitmap = it,
                rect = croppedRect,
            )
        } ?: GifticonCropData.None,
        name = name,
        brand = brand,
        barcode = barcode,
        expired = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
    )
}