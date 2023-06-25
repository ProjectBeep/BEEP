package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImageRepositoryImpl @Inject constructor(
    private val pagingSource: GalleryImagePagingSource,
) : GalleryImageRepository {

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return pagingSource.getImages(pageSize)
    }
}
