package com.lighthouse.beep.ui.feature.editor.model

import android.net.Uri
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.EMPTY_RECT
import com.lighthouse.beep.core.common.exts.ofDate
import com.lighthouse.beep.core.common.exts.ofMonth
import com.lighthouse.beep.core.common.exts.ofYear
import com.lighthouse.beep.core.common.exts.toFormattedString
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
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
    val expireAt: Date = EMPTY_DATE,
    val expireAtCropData: GifticonCropData = GifticonCropData.None,
    val isCashCard: Boolean = false,
    val balance: String = "",
    val balanceCropData: GifticonCropData = GifticonCropData.None,
    val memo: String = "",
) {
    val displayBarcode: String = TextInputFormat.BARCODE.valueToTransformed(barcode)

    val displayExpired: String = if (expireAt == EMPTY_DATE) "" else expireAt.toFormattedString()

    val displayBalance: String = TextInputFormat.BALANCE.valueToTransformed(balance)

    val expireYear: String = if (expireAt == EMPTY_DATE) "" else expireAt.ofYear().toString()

    val expireMonth: String =
        if (expireAt == EMPTY_DATE) "" else String.format("%02d", expireAt.ofMonth())

    val expireDate: String =
        if (expireAt == EMPTY_DATE) "" else String.format("%02d", expireAt.ofDate())

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
        expireAt = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
    )
}

internal fun GifticonData.toEditInfo(): GifticonEditInfo {
    return GifticonEditInfo(
        thumbnailType = when(thumbnail) {
            is EditGifticonThumbnail.BuiltIn -> GifticonThumbnail.TYPE_BUILT_IN
            else -> GifticonThumbnail.TYPE_IMAGE
        },
        thumbnailBuiltInCode = when(thumbnail) {
            is EditGifticonThumbnail.BuiltIn -> thumbnail.builtIn.code
            else -> ""
        },
        thumbnailBitmap = when(thumbnail) {
            is EditGifticonThumbnail.Crop -> thumbnail.bitmap
            else -> null
        },
        thumbnailUri = null,
        thumbnailRect = when(thumbnail) {
            is EditGifticonThumbnail.Crop -> thumbnail.rect
            else -> EMPTY_RECT
        },
        gifticonUri = null,
        originUri = originUri,
        imagePath = imagePath,
        name = name,
        displayBrand = brand,
        barcode = displayBarcode,
        isCashCard = isCashCard,
        totalCash = balance.toIntOrNull() ?: 0,
        remainCash = balance.toIntOrNull() ?: 0,
        memo = memo,
        expireAt = expireAt,
    )
}