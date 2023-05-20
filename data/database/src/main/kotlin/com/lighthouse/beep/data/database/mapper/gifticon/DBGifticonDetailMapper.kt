package com.lighthouse.beep.data.database.mapper.gifticon

import com.lighthouse.beep.data.database.entity.DBGifticonEntity
import com.lighthouse.beep.data.database.model.DBGifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonDetail
import java.util.Date

internal fun DBGifticonDetail.toModel(): GifticonDetail {
    return GifticonDetail(
        id = id,
        userId = userId,
        croppedUri = croppedUri,
        croppedRect = croppedRect,
        originUri = originUri,
        name = name,
        displayBrand = displayBrand,
        barcode = barcode,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        memo = memo,
        isUsed = isUsed,
        expireAt = expireAt,
        createdAt = createdAt,
    )
}

internal fun DBGifticonDetail.toEntity(): DBGifticonEntity {
    return DBGifticonEntity(
        id = id,
        userId = userId,
        croppedUri = croppedUri,
        croppedRect = croppedRect,
        originUri = originUri,
        name = name,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
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
