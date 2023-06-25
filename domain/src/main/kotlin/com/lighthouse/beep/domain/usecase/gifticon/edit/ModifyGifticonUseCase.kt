package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.auth.model.exception.AuthenticationException
import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import javax.inject.Inject

class ModifyGifticonUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(gifticonForUpdate: GifticonForUpdate): Result<Unit> {
        return if (authRepository.getCurrentUserId() == gifticonForUpdate.userId) {
            gifticonRepository.updateGifticon(gifticonForUpdate)
        } else {
            Result.failure(AuthenticationException("UserId 가 기프티콘 소유주와 다릅니다."))
        }
    }
}
