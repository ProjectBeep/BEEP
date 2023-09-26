package com.lighthouse.beep.data.local.repository.gallery

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lighthouse.beep.data.repository.gallery.GalleryImageDataSource
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageBucket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImageDataSourceImpl @Inject constructor(
    private val dataSource: GalleryDataSource,
) : GalleryImageDataSource {

    override suspend fun getFolders(): List<GalleryImageBucket> {
        return dataSource.getFolders()
    }

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                GalleryImagePagingFactory(dataSource, 0, pageSize)
            },
        ).flow
    }

    override fun getImages(bucketId: Long, pageSize: Int): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                GalleryImageByBucketIdPagingFactory(dataSource, bucketId, 0, pageSize)
            },
        ).flow
    }

    override suspend fun getImages(page: Int, limit: Int): List<GalleryImage> {
        return dataSource.getImages(page, limit)
    }
}
