package com.lighthouse.beep.data.repository.gallery

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryRecognize

interface GalleryRecognizeDataSource {
    suspend fun getRecognizeDataList(): List<GalleryRecognize>

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}