package com.lighthouse.domain.usecase.edit.addgifticon

import com.lighthouse.beep.model.gifticon.GifticonForAddition
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.gifticon.GifticonEditRepository
import javax.inject.Inject

class SaveGifticonsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonEditRepository
) {

    suspend operator fun invoke(gifticons: List<GifticonForAddition>): Result<Unit> {
        return gifticonRepository.insertGifticons(
            authRepository.getCurrentUserId(),
            gifticons
        )
    }
}
