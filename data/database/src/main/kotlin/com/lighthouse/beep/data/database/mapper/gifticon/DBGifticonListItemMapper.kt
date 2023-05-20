package com.lighthouse.beep.data.database.mapper.gifticon

import com.lighthouse.beep.data.database.model.DBGifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonListItem

internal fun List<DBGifticonListItem>.toModel(): List<GifticonListItem> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonListItem.toModel(): GifticonListItem {
    return GifticonListItem(
        id = id,
        croppedUri = croppedUri,
        name = name,
        displayBrand = displayBrand,
        expireAt = expireAt,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        dDay = dDay,
    )
}
