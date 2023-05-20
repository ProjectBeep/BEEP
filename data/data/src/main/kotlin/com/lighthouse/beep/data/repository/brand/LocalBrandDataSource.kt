package com.lighthouse.beep.data.repository.brand

import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.Dms

interface LocalBrandDataSource {

    suspend fun getSectionId(brand: String, x: Dms, y: Dms): Long?

    suspend fun getBrandPlaceInfo(sectionId: Long): List<BrandLocation>

    suspend fun getBrandPlaceInfo(brand: String, x: Dms, y: Dms): List<BrandLocation>

    suspend fun insertSection(brand: String, x: Dms, y: Dms): Long

    suspend fun insertBrandPlaceInfo(
        brandLocationList: List<BrandLocation>,
        gap: Int,
    )

    suspend fun removeExpirationBrands()
}
