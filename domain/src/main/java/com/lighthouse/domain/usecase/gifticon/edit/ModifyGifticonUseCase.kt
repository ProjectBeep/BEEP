package com.lighthouse.domain.usecase.gifticon.edit

import com.lighthouse.beep.model.exception.auth.AuthenticationException
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.gifticon.GifticonEditRepository
import javax.inject.Inject

class ModifyGifticonUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonEditRepository
) {

    suspend operator fun invoke(gifticonForUpdate: GifticonForUpdate): Result<Unit> {
        return if (authRepository.getCurrentUserId() == gifticonForUpdate.userId) {
            gifticonRepository.updateGifticon(gifticonForUpdate)
        } else {
            Result.failure(AuthenticationException("UserId 가 기프티콘 소유주와 다릅니다."))
        }
    }
}
