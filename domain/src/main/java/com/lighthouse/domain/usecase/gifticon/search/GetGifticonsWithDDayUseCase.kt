package com.lighthouse.domain.usecase.gifticon.search

import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.gifticon.GifticonSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGifticonsWithDDayUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonSearchRepository
) {

    operator fun invoke(): Flow<List<GifticonNotification>> {
        return gifticonRepository.getGifticonsWithDDay(
            authRepository.getCurrentUserId(),
            setOf(0, 1, 3, 7, 14)
        )
    }
}
