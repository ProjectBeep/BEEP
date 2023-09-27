package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryImageDataSource {

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>

    suspend fun getImages(page: Int, limit: Int): List<GalleryImage>

    fun getImageSize(): Int
}
