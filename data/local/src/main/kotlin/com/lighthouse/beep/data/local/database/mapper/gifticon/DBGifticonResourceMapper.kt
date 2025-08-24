package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonResource
import com.lighthouse.beep.data.model.gifticon.GifticonResource

internal fun List<DBGifticonResource>.toModel(): List<GifticonResource> {
    return map {
        it.toModel()
    }
}

internal fun DBGifticonResource.toModel(): GifticonResource {
    return GifticonResource(
        gifticonUri = originUri ?: android.net.Uri.EMPTY,
        thumbnailUri = thumbnailUri,
    )
}