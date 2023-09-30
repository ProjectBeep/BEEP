package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import javax.inject.Inject

class GetGalleryRecognizeDataUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
){

    suspend operator fun invoke(image: GalleryImage): GalleryImageRecognizeData {
        return galleryRepository.getRecognizeData(image)
    }
}