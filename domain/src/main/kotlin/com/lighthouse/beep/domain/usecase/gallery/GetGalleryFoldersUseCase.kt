package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.model.gallery.GalleryImageBucket
import javax.inject.Inject

class GetGalleryFoldersUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository
){

    suspend operator fun invoke(): List<GalleryImageBucket> {
        return galleryRepository.getFolders()
    }
}