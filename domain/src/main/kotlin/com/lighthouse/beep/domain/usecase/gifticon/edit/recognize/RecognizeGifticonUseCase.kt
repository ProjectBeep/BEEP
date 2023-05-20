package com.lighthouse.beep.domain.usecase.gifticon.edit.recognize

import com.lighthouse.beep.domain.repository.gifticon.GifticonRecognizeRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import javax.inject.Inject

class RecognizeGifticonUseCase @Inject constructor(
    private val gifticonRecognizeRepository: GifticonRecognizeRepository,
) {

    suspend operator fun invoke(gallery: GalleryImage): Result<GifticonRecognizeResult> {
        return gifticonRecognizeRepository.recognize(gallery)
    }
}
