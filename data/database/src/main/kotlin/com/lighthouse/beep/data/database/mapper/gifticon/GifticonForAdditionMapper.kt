package com.lighthouse.beep.data.database.mapper.gifticon

import com.lighthouse.beep.data.database.entity.DBGifticonEntity
import com.lighthouse.beep.model.gifticon.GifticonForAddition
import com.lighthouse.beep.model.gifticon.GifticonImageCreateResult
import java.util.Date

internal fun GifticonForAddition.toEntity(
    userId: String,
    imageCreateResult: GifticonImageCreateResult,
): DBGifticonEntity {
    val createdAt = Date()
    return DBGifticonEntity(
        id = null,
        userId = userId,
        croppedUri = imageCreateResult.outputCropUri,
        croppedRect = imageCreateResult.outputCropRect,
        originUri = imageCreateResult.outputOriginUri,
        name = name,
        brand = brandName.lowercase(),
        displayBrand = brandName,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        memo = memo,
        isUsed = false,
        expireAt = expireAt,
        updatedAt = createdAt,
        createdAt = createdAt,
    )
}
