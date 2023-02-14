package com.lighthouse.data.database.mapper.gifticon.search

import com.lighthouse.beep.model.gifticon.Gifticon
import com.lighthouse.common.mapper.toDomain
import com.lighthouse.data.database.model.DBGifticon

internal fun List<DBGifticon>.toDomain(): List<Gifticon> {
    return map {
        it.toDomain()
    }
}

internal fun DBGifticon.toDomain(): Gifticon {
    return Gifticon(
        id,
        createdAt,
        userId,
        hasImage,
        croppedUri.toDomain(),
        name,
        brand,
        expireAt,
        barcode,
        isCashCard,
        balance,
        memo,
        isUsed,
        dDay
    )
}
