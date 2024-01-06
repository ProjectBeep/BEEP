package com.lighthouse.beep.data.repository.gallery

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageData

interface GalleryRecognizeDataSource {
    suspend fun getRecognizeDataList(): List<GalleryImageData>

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}