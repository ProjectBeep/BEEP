package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageBucket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImageRepositoryImpl @Inject constructor(
    private val dataSource: GalleryImageDataSource,
) : GalleryImageRepository {

    override suspend fun getFolders(): List<GalleryImageBucket> {
        return dataSource.getFolders()
    }

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return dataSource.getImages(pageSize)
    }

    override fun getImages(bucketId: Long, pageSize: Int): Flow<PagingData<GalleryImage>> {
        return dataSource.getImages(bucketId, pageSize)
    }

    override suspend fun getImages(page: Int, limit: Int): List<GalleryImage> {
        return dataSource.getImages(page, limit)
    }
}
