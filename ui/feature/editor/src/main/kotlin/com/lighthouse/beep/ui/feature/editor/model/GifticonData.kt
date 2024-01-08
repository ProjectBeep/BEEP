package com.lighthouse.beep.ui.feature.editor.model

import android.net.Uri
import com.google.mlkit.vision.barcode.common.Barcode
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.EMPTY_RECT
import com.lighthouse.beep.core.common.exts.ofDate
import com.lighthouse.beep.core.common.exts.ofMonth
import com.lighthouse.beep.core.common.exts.ofYear
import com.lighthouse.beep.core.common.exts.toFormattedString
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonBarcodeType
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import java.util.Date

internal data class GifticonData(
    val originUri: Uri,
    val thumbnailUri: Uri? = null,
    val imagePath: String,
    val imageAddedDate: Date,
    val thumbnail: EditGifticonThumbnail = EditGifticonThumbnail.Default(originUri),
    val thumbnailCropData: GifticonCropData = GifticonCropData.None,
    val name: String = "",
    val nameCropData: GifticonCropData = GifticonCropData.None,
    val brand: String = "",
    val brandCropData: GifticonCropData = GifticonCropData.None,
    val barcodeType: GifticonBarcodeType = GifticonBarcodeType.CODE_128,
    val barcode: String = "",
    val barcodeCropData: GifticonCropData = GifticonCropData.None,
    val expireAt: Date = EMPTY_DATE,
    val expireAtCropData: GifticonCropData = GifticonCropData.None,
    val isCashCard: Boolean = false,
    val balance: String = "",
    val balanceCropData: GifticonCropData = GifticonCropData.None,
    val remainCash: String = "",
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

internal fun GifticonRecognizeInfo?.toData(
    originUri: Uri,
    imagePath: String,
    imageAddedDate: Date,
): GifticonData {
    this ?: return GifticonData(
        originUri = originUri,
        imagePath = imagePath,
        imageAddedDate = imageAddedDate,
    )
    return GifticonData(
        originUri = originUri,
        imagePath = imagePath,
        imageAddedDate = imageAddedDate,
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
        barcodeType = when(barcodeType) {
            Barcode.FORMAT_QR_CODE -> GifticonBarcodeType.QR_CODE
            else -> GifticonBarcodeType.CODE_128
        },
        barcode = barcode,
        expireAt = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
        remainCash = balance.toString(),
    )
}

internal fun GifticonData.toEditInfo(): GifticonEditInfo {
    return GifticonEditInfo(
        thumbnailType = when (thumbnail) {
            is EditGifticonThumbnail.BuiltIn -> GifticonThumbnail.TYPE_BUILT_IN
            else -> GifticonThumbnail.TYPE_IMAGE
        },
        thumbnailBuiltInCode = when (thumbnail) {
            is EditGifticonThumbnail.BuiltIn -> thumbnail.builtIn.code
            else -> ""
        },
        thumbnailBitmap = when (thumbnail) {
            is EditGifticonThumbnail.Crop -> thumbnail.bitmap
            else -> null
        },
        thumbnailUri = thumbnailUri,
        thumbnailRect = when (thumbnail) {
            is EditGifticonThumbnail.Crop -> thumbnail.rect
            else -> EMPTY_RECT
        },
        gifticonUri = originUri,
        originUri = originUri,
        imagePath = imagePath,
        imageAddedDate = imageAddedDate,
        name = name,
        displayBrand = brand,
        barcodeType = barcodeType.name,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = if(isCashCard) balance.toIntOrNull() ?: 0 else 0,
        remainCash = if(isCashCard) remainCash.toIntOrNull() ?: 0 else 0,
        memo = memo,
        expireAt = expireAt,
    )
}

internal fun GifticonEditInfo?.toData(): GifticonData? {
    this ?: return null

    return GifticonData(
        originUri = originUri,
        thumbnailUri = thumbnailUri,
        imagePath = imagePath,
        imageAddedDate = imageAddedDate,
        thumbnail = when {
            thumbnailType == GifticonThumbnail.TYPE_IMAGE && thumbnailUri != null -> {
                EditGifticonThumbnail.Default(
                    originUri = originUri,
                    thumbnailUri = thumbnailUri,
                )
            }

            else -> {
                EditGifticonThumbnail.BuiltIn(
                    GifticonBuiltInThumbnail.of(thumbnailBuiltInCode)
                )
            }
        },
        thumbnailCropData = when {
            thumbnailType == GifticonThumbnail.TYPE_IMAGE && thumbnailUri != null -> {
                GifticonCropData(
                    rect = thumbnailRect,
                    zoom = 1f,
                )
            }
            else -> GifticonCropData.None
        },
        name = name,
        brand = displayBrand,
        barcodeType = GifticonBarcodeType.of(barcodeType),
        barcode = barcode,
        expireAt = expireAt,
        isCashCard = isCashCard,
        balance = totalCash.toString(),
        remainCash = remainCash.toString(),
        memo = memo,
    )
}