package com.lighthouse.beep.data.remote.repository

import com.lighthouse.beep.data.remote.api.KakaoApiService
import com.lighthouse.beep.data.remote.mapper.toBrandPlaceInfoList
import com.lighthouse.beep.data.repository.brand.RemoteBrandDataSource
import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.DmsRect
import javax.inject.Inject

internal class RemoteBrandDataSourceImpl @Inject constructor(
    private val kakaoApiService: KakaoApiService,
) : RemoteBrandDataSource {

    override suspend fun getBrandPlaceInfo(
        brandName: String,
        rect: DmsRect,
        offset: Int,
    ): Result<List<BrandLocation>> {
        return runCatching {
            kakaoApiService.getAllBrandPlaceInfo(
                brandName,
                rect.format,
                offset,
            ).toBrandPlaceInfoList(brandName)
        }
    }
}
