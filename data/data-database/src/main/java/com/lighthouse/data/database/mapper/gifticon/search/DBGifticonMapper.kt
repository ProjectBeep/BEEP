package com.lighthouse.data.database.mapper.gifticon.search

import com.lighthouse.beep.model.gifticon.Gifticon
import com.lighthouse.data.database.model.DBGifticon

internal fun List<DBGifticon>.toDomain(): List<Gifticon> {
    return map {
        it.toDomain()
    }
}

internal fun DBGifticon.toDomain(): Gifticon {
    return Gifticon(
        id = id,
        croppedUri = croppedUri.toString(),
        name = name,
        brand = brand,
        expireAt = expireAt,
        isCashCard = isCashCard,
        totalCash = totalCash,
        remainCash = remainCash,
        dDay = dDay
    )
}
