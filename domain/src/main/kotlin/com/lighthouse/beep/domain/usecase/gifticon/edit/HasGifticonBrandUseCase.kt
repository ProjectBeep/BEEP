package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.domain.repository.auth.AuthRepository
import com.lighthouse.beep.domain.repository.gifticon.GifticonRepository
import javax.inject.Inject

class HasGifticonBrandUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonRepository,
) {
    suspend operator fun invoke(brand: String): Result<Boolean> {
        return gifticonRepository.hasGifticonBrand(authRepository.getCurrentUserId(), brand)
    }
}
