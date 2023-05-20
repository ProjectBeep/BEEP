package com.lighthouse.beep.domain.usecase.gifticon.edit.recognize

import android.net.Uri
import com.lighthouse.beep.domain.repository.gifticon.GifticonRecognizeRepository
import java.util.Date
import javax.inject.Inject

class RecognizeExpiredUseCase @Inject constructor(
    private val gifticonRecognizeRepository: GifticonRecognizeRepository,
) {

    suspend operator fun invoke(uri: Uri): Result<Date> {
        return gifticonRecognizeRepository.recognizeExpired(uri)
    }
}
