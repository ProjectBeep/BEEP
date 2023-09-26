package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import javax.inject.Inject

class GetGalleryImageSizeUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
) {

    operator fun invoke(): Int {
        return galleryRepository.getImageSize()
    }
}