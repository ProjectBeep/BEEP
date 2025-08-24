package com.lighthouse.beep.data.local.database.mapper.gifticon

import android.net.Uri
import com.lighthouse.beep.data.local.database.model.DBGifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonEditInfo

// 새로운 구조에서는 DBGifticonEntity 대신 DBItemEntity + DBGifticonDetailEntity 사용
internal fun List<GifticonEditInfo>.toEntityForCreate(
    userId: String,
): List<DBGifticonEditInfo> {
    return map {
        it.toEntityForCreate(userId)
    }
}

internal fun GifticonEditInfo.toEntityForCreate(
    userId: String,
): DBGifticonEditInfo {
    return DBGifticonEditInfo(
        gifticonId = 0, // 새로 생성할 때는 0
        name = name,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
        barcode = barcode,
        barcodeType = barcodeType,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        isUsed = false,
        memo = memo,
        expireAt = expireAt,
        originUri = originUri,
        croppedRect = thumbnailRect,
    ).apply {
        this.userId = userId
        this.croppedUri = gifticonUri
        this.thumbnailUri = thumbnailUri
    }
}

internal fun GifticonEditInfo.toEntity(
    gifticonId: Long,
): DBGifticonEditInfo {
    return DBGifticonEditInfo(
        gifticonId = gifticonId,
        name = name,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
        barcode = barcode,
        barcodeType = barcodeType,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        isUsed = false,
        memo = memo,
        expireAt = expireAt,
        originUri = originUri,
        croppedRect = thumbnailRect,
    ).apply {
        this.croppedUri = gifticonUri
        this.thumbnailUri = thumbnailUri
    }
}

internal fun DBGifticonEditInfo.toModel(): GifticonEditInfo {
    return GifticonEditInfo(
        thumbnailType = "IMAGE", // 기본값
        thumbnailBuiltInCode = "",
        thumbnailBitmap = null,
        thumbnailUri = thumbnailUri,
        thumbnailRect = croppedRect ?: android.graphics.Rect(),
        gifticonUri = croppedUri,
        originUri = originUri ?: Uri.EMPTY,
        imagePath = "",
        imageAddedDate = java.util.Date(),
        name = name,
        displayBrand = displayBrand,
        barcodeType = barcodeType,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash ?: 0,
        remainCash = remainCash ?: 0,
        memo = memo ?: "",
        expireAt = expireAt,
    )
}