package com.lighthouse.domain.usecase.gifticon.edit

import com.lighthouse.beep.model.gifticon.GifticonWithCrop
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.gifticon.GifticonSearchRepository
import javax.inject.Inject

class GetGifticonForUpdateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonSearchRepository
) {

    suspend operator fun invoke(gifticonId: String): Result<GifticonWithCrop> {
        return gifticonRepository.getGifticonCrop(
            authRepository.getCurrentUserId(),
            gifticonId
        )
    }
}
