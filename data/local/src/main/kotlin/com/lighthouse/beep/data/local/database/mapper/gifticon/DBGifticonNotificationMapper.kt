package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonNotification
import com.lighthouse.beep.model.gifticon.GifticonNotification

internal fun List<DBGifticonNotification>.toModel(): List<GifticonNotification> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonNotification.toModel(): GifticonNotification {
    return GifticonNotification(
        id,
        name,
        dDay,
    )
}
