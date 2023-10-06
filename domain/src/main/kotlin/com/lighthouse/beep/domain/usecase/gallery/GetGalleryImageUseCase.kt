package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import javax.inject.Inject

class GetGalleryImageUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository
){
    suspend operator fun invoke(id: Long): GalleryImage? {
        return galleryRepository.getImage(id)
    }
}