package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonThumbnail

internal fun List<DBGifticonListItem>.toModel(): List<GifticonListItem> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonListItem.toModel(): GifticonListItem {
    return GifticonListItem(
        id = id,
        userId = "", // 새 모델에서는 Item 레벨에서 관리
        isCashCard = isCashCard,
        remainCash = remainCash ?: 0,
        totalCash = totalCash ?: 0,
        thumbnail = when {
            thumbnailUri != null -> {
                GifticonThumbnail.Image(
                    uri = thumbnailUri,
                    rect = android.graphics.Rect(), // 기본값 사용
                )
            }
            else -> {
                GifticonThumbnail.BuildIn("")
            }
        },
        name = name,
        displayBrand = displayBrand,
        isUsed = isUsed,
        expireAt = expireAt,
    )
}
