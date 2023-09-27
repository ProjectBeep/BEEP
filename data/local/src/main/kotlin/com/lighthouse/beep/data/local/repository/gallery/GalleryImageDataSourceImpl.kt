package com.lighthouse.beep.data.local.repository.gallery

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lighthouse.beep.data.repository.gallery.GalleryImageDataSource
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImageDataSourceImpl @Inject constructor(
    private val dataSource: GalleryDataSource,
) : GalleryImageDataSource {

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                GalleryImagePagingFactory(dataSource, 0, pageSize)
            },
        ).flow
    }

    override suspend fun getImages(page: Int, limit: Int): List<GalleryImage> {
        return dataSource.getImages(page, limit)
    }

    override fun getImageSize(): Int {
        return dataSource.getImageSize()
    }
}
