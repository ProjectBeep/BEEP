package com.lighthouse.beep.domain.usecase.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGalleryImagesByBucketIdUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository
){

    operator fun invoke(bucketId: Long, pageSize: Int): Flow<PagingData<GalleryImage>> {
        return galleryRepository.getImages(bucketId, pageSize)
    }
}