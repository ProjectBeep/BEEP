package com.lighthouse.beep.domain.usecase.gifticon.remove

import com.lighthouse.beep.data.repository.brand.BrandRepository
import javax.inject.Inject

class RemoveExpirationBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
) {

    suspend operator fun invoke() {
        brandRepository.removeExpirationBrands()
    }
}
