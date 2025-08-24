package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonImageData
import com.lighthouse.beep.model.gifticon.GifticonImageData

internal fun List<DBGifticonImageData>.toModel(): List<GifticonImageData> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonImageData.toModel(): GifticonImageData {
    return GifticonImageData(
        id = id,
        imagePath = originUri?.toString() ?: "",
        imageAddedDate = java.util.Date(),
    )
}
