package com.lighthouse.beep.domain.usecase.gifticon.edit

import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import javax.inject.Inject

class HasGifticonBrandUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository,
) {
    suspend operator fun invoke(brand: String): Result<Boolean> {
        val userUid = userRepository.getAuthInfo()
        return gifticonRepository.hasGifticonBrand(userUid.userUid, brand)
    }
}
