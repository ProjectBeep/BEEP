package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.auth.model.exception.AuthenticationException
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import javax.inject.Inject

class ModifyGifticonUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(gifticonForUpdate: GifticonForUpdate): Result<Unit> {
        val userInfo = userRepository.getAuthInfo()
        return if (userInfo.userUid == gifticonForUpdate.userId) {
            gifticonRepository.updateGifticon(gifticonForUpdate)
        } else {
            Result.failure(AuthenticationException("UserId 가 기프티콘 소유주와 다릅니다."))
        }
    }
}
