package com.lighthouse.beep.domain.usecase.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGalleryImagesUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository
){

    operator fun invoke(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return galleryRepository.getImages(pageSize)
    }

    suspend operator fun invoke(page: Int, limit: Int, offset: Int): List<GalleryImage> {
        return galleryRepository.getImages(page, limit, offset)
    }
}