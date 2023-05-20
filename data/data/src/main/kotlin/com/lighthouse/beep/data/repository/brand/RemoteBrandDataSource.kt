package com.lighthouse.beep.data.repository.brand

import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.DmsRect

interface RemoteBrandDataSource {

    suspend fun getBrandPlaceInfo(
        brandName: String,
        rect: DmsRect,
        offset: Int = 10,
    ): Result<List<BrandLocation>>
}
