package com.lighthouse.beep.data.database.mapper.gifticon

import com.lighthouse.beep.data.database.entity.DBGifticonEntity
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import com.lighthouse.beep.model.gifticon.GifticonImageUpdateResult
import java.util.Date

internal fun GifticonForUpdate.toEntity(
    imageUpdateResult: GifticonImageUpdateResult?,
): DBGifticonEntity {
    return DBGifticonEntity(
        id = id,
        userId = userId,
        croppedUri = imageUpdateResult?.outputCropUri ?: croppedUri,
        croppedRect = imageUpdateResult?.outputCropRect ?: croppedRect,
        originUri = originUri,
        name = name,
        brand = brandName.lowercase(),
        displayBrand = brandName,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        memo = memo,
        isUsed = isUsed,
        expireAt = expireAt,
        updatedAt = Date(),
        createdAt = createdAt,
    )
}
