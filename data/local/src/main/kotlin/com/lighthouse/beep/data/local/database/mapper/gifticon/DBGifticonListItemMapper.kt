package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.model.gifticon.GifticonType

internal fun List<DBGifticonListItem>.toModel(): List<GifticonListItem> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonListItem.toModel(): GifticonListItem {
    return GifticonListItem(
        id = id,
        userId = userId,
        type = when (isCashCard) {
            true -> GifticonType.Cash(
                remain = remainCash,
                total = totalCash,
            )

            false -> GifticonType.Product
        },
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
        isUsed = isUsed,
        expireAt = expireAt,
    )
}
