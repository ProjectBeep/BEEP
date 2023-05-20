package com.lighthouse.beep.domain.usecase.gifticon.edit.recognize

import android.net.Uri
import com.lighthouse.beep.domain.repository.gifticon.GifticonRecognizeRepository
import javax.inject.Inject

class RecognizeBalanceUseCase @Inject constructor(
    private val gifticonRecognizeRepository: GifticonRecognizeRepository,
) {

    suspend operator fun invoke(uri: Uri): Result<Int> {
        return gifticonRecognizeRepository.recognizeBalance(uri)
    }
}
