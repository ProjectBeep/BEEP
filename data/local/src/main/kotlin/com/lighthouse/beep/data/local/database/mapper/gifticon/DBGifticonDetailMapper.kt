package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonThumbnail

internal fun DBGifticonDetail.toModel(): GifticonDetail {
    return GifticonDetail(
        id = id,
        userId = userId,
        isCashCard = isCashCard,
        remainCash = remainCash,
        totalCash = totalCash,
        thumbnail = when {
            thumbnailType == GifticonThumbnail.TYPE_IMAGE && thumbnailUri != null -> {
                GifticonThumbnail.Image(
                    uri = thumbnailUri,
                    rect = thumbnailRect,
                )
            }

            else -> {
                GifticonThumbnail.BuildIn(thumbnailBuiltInCode)
            }
        },
        name = name,
        displayBrand = displayBrand,
        barcode = barcode,
        memo = memo,
        isUsed = isUsed,
        expireAt = expireAt,
    )
}
