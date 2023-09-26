package com.lighthouse.beep.data.repository.gallery

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData

interface GalleryRecognizeDataSource {
    suspend fun getRecognizeData(data: GalleryImage): GalleryImageRecognizeData

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}