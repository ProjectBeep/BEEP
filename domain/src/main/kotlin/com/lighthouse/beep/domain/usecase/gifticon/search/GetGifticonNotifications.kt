package com.lighthouse.beep.domain.usecase.gifticon.search

import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.gifticon.GifticonNotification
import javax.inject.Inject

class GetGifticonNotifications @Inject constructor(
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(): List<GifticonNotification> {
        val userInfo = userRepository.getAuthInfo()
        return gifticonRepository.getGifticonNotifications(userInfo.userUid, setOf(0, 1, 3, 7, 14))
    }
}
