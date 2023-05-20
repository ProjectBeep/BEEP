package com.lighthouse.beep.domain.repository.brand

import com.lighthouse.beep.model.brand.BrandLocation
import kotlinx.coroutines.flow.Flow

interface BrandRepository {

    suspend fun searchBrandLocationList(
        brandNames: List<String>,
        xDD: Double,
        yDD: Double,
        cachedSectionIds: Set<Long>,
        depth: Int,
        gap: Int,
    ): Result<List<BrandLocation>>

    fun searchBrandLocationListWithFlow(
        brandNames: List<String>,
        xDD: Double,
        yDD: Double,
        cachedSectionIds: Set<Long>,
        depth: Int,
        gap: Int,
    ): Flow<Result<List<BrandLocation>>>

    suspend fun removeExpirationBrands()
}
