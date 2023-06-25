package com.lighthouse.beep.data.local.repository.gallery

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lighthouse.beep.data.repository.gallery.GalleryImagePagingSource
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImagePagingSourceImpl @Inject constructor(
    private val dataSource: GalleryImageDataSource,
) : GalleryImagePagingSource {

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                GalleryImagePagingFactory(dataSource, 0, pageSize)
            },
        ).flow
    }
}
