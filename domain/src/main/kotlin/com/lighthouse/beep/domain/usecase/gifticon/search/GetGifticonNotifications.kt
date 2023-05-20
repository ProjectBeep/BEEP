package com.lighthouse.beep.domain.usecase.gifticon.search

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.domain.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonNotification
import javax.inject.Inject

class GetGifticonNotifications @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonRepository,
) {

    suspend operator fun invoke(): List<GifticonNotification> {
        return gifticonRepository.getGifticonNotifications(
            authRepository.getCurrentUserId(),
            setOf(0, 1, 3, 7, 14),
        )
    }
}
