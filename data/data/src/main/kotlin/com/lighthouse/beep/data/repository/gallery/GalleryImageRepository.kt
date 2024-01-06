package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageData
import kotlinx.coroutines.flow.Flow

interface GalleryImageRepository {

    suspend fun getImage(id: Long): GalleryImage?

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>

    suspend fun getImages(page: Int, limit: Int, offset: Int): List<GalleryImage>

    fun getImageSize(): Int

    suspend fun getRecognizeDataList(): List<GalleryImageData>

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}
