package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.data.local.database.entity.DBGifticonEntity
import com.lighthouse.beep.data.local.database.model.DBGifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import java.util.Date

internal fun GifticonEditInfo.toEntityForCreate(
    userId: String,
): DBGifticonEntity {
    return DBGifticonEntity(
        id = null,
        userId = userId,
        thumbnailType = thumbnailType,
        thumbnailBuiltInCode = thumbnailBuiltInCode,
        thumbnailUri = thumbnailUri,
        thumbnailRect = thumbnailRect,
        gifticonUri = gifticonUri,
        imagePath = imagePath,
        name = name,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        memo = memo,
        isUsed = false,
        usedAt = EMPTY_DATE,
        expireAt = expireAt,
        updatedAt = Date(),
        createdAt = Date(),
    )
}

internal fun GifticonEditInfo.toEntity(
    gifticonId: Long,
): DBGifticonEditInfo {
    return DBGifticonEditInfo(
        id = gifticonId,
        thumbnailType = thumbnailType,
        thumbnailBuiltInCode = thumbnailBuiltInCode,
        thumbnailUri = thumbnailUri,
        thumbnailRect = thumbnailRect,
        gifticonUri = gifticonUri,
        imagePath = imagePath,
        name = name,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        memo = memo,
        expireAt = expireAt,
        updatedAt = Date(),
    )
}