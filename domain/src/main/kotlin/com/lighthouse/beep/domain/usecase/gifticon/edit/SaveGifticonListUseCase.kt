package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.gifticon.GifticonForAddition
import javax.inject.Inject

class SaveGifticonListUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(gifticonList: List<GifticonForAddition>): Result<Unit> {
        val authInfo = userRepository.getAuthInfo()

        return gifticonRepository.insertGifticonList(
            authInfo.userUid,
            gifticonList,
        )
    }
}
