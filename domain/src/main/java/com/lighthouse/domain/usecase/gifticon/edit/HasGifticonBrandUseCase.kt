package com.lighthouse.domain.usecase.gifticon.edit

import com.lighthouse.domain.repository.auth.AuthRepository
import com.lighthouse.domain.repository.gifticon.GifticonSearchRepository
import javax.inject.Inject

class HasGifticonBrandUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gifticonRepository: GifticonSearchRepository
) {
    suspend operator fun invoke(brand: String): Result<Boolean> {
        return gifticonRepository.hasGifticonBrand(authRepository.getCurrentUserId(), brand)
    }
}
