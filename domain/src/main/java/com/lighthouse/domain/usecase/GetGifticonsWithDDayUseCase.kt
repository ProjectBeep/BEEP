package com.lighthouse.domain.usecase

import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.domain.repository.gifticon.GifticonSearchRepository
import com.lighthouse.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGifticonsWithDDayUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonSearchRepository
) {

    operator fun invoke(): Flow<List<GifticonNotification>> {
        return gifticonRepository.getGifticonsWithDDay(
            userRepository.getUserId(),
            setOf(0, 1, 3, 7, 14)
        )
    }
}
