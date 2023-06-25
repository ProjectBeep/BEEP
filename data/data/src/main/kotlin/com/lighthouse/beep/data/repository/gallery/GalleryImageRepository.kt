package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryImageRepository {

    fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>>
}
