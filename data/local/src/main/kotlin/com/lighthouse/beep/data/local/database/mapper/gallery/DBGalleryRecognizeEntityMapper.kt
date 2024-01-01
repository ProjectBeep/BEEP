package com.lighthouse.beep.data.local.database.mapper.gallery

import com.lighthouse.beep.data.local.database.entity.DBGalleryRecognizeEntity
import com.lighthouse.beep.model.gallery.GalleryRecognize

internal fun List<DBGalleryRecognizeEntity>.toModel(): List<GalleryRecognize> {
    return map {
        it.toModel()
    }
}

internal fun DBGalleryRecognizeEntity.toModel(): GalleryRecognize {
    return GalleryRecognize(
        imagePath = imagePath,
        dateAdded = dateAdded,
        isGifticon = isGifticon,
    )
}