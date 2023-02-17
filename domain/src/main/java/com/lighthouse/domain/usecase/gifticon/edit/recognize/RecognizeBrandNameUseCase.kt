package com.lighthouse.domain.usecase.gifticon.edit.recognize

import com.lighthouse.domain.repository.gifticon.GifticonRecognizeRepository
import javax.inject.Inject

class RecognizeBrandNameUseCase @Inject constructor(
    private val gifticonRecognizeRepository: GifticonRecognizeRepository
) {

    suspend operator fun invoke(uri: String): Result<String> {
        return gifticonRecognizeRepository.recognizeText(uri)
    }
}
