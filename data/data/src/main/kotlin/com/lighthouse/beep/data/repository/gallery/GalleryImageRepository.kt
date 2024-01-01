package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import com.lighthouse.beep.model.gallery.GalleryRecognize
import kotlinx.coroutines.flow.Flow

interface GalleryImageRepository {

    suspend fun getImage(id: Long): GalleryImage?

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>

    suspend fun getImages(page: Int, limit: Int, offset: Int): List<GalleryImage>

    fun getImageSize(): Int

    suspend fun getRecognizeData(data: GalleryImage): GalleryImageRecognizeData

    suspend fun getRecognizeDataList(): List<GalleryRecognize>

    suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean)
}
