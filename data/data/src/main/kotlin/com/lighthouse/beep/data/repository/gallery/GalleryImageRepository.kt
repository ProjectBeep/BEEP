package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import kotlinx.coroutines.flow.Flow

interface GalleryImageRepository {

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>

    suspend fun getImages(page: Int, limit: Int): List<GalleryImage>

    fun getImageSize(): Int

    suspend fun getRecognizeData(data: GalleryImage): GalleryImageRecognizeData

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}
