package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageBucket
import kotlinx.coroutines.flow.Flow

interface GalleryImageDataSource {

    suspend fun getFolders(): List<GalleryImageBucket>

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>

    fun getImages(bucketId: Long, pageSize: Int): Flow<PagingData<GalleryImage>>

    suspend fun getImages(page: Int, limit: Int): List<GalleryImage>
}
