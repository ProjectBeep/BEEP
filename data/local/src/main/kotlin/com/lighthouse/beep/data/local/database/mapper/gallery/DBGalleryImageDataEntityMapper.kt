package com.lighthouse.beep.data.local.database.mapper.gallery

import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity
import com.lighthouse.beep.model.gallery.GalleryImageData

internal fun List<DBGalleryImageDataEntity>.toModel(): List<GalleryImageData> {
    return map {
        it.toModel()
    }
}

internal fun DBGalleryImageDataEntity.toModel(): GalleryImageData {
    return GalleryImageData(
        imagePath = imagePath,
        addedDate = java.util.Date(dateAdded),
        isGifticon = isGifticon,
    )
}