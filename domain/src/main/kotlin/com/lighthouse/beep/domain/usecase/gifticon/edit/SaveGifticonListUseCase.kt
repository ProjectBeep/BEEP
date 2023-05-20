package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.domain.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonForAddition
import javax.inject.Inject

class SaveGifticonListUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(gifticonList: List<GifticonForAddition>): Result<Unit> {
        return gifticonRepository.insertGifticonList(
            authRepository.getCurrentUserId(),
            gifticonList,
        )
    }
}
