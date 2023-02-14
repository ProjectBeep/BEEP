package com.lighthouse.data.database.mapper.gifticon.search

import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.data.database.model.DBGifticonNotification

internal fun List<DBGifticonNotification>.toDomain(): List<GifticonNotification> {
    return map {
        it.toDomain()
    }
}

internal fun DBGifticonNotification.toDomain(): GifticonNotification {
    return GifticonNotification(
        id,
        name,
        dDay
    )
}
