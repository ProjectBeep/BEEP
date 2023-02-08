package com.lighthouse.data.remote.mapper

import com.lighthouse.beep.model.brand.BrandPlaceInfo
import com.lighthouse.data.remote.model.BrandPlaceInfoDataContainer

internal fun List<BrandPlaceInfoDataContainer.BrandPlaceInfoData>.toDomain(
    brandName: String
): List<BrandPlaceInfo> {
    return this.map {
        BrandPlaceInfo(
            addressName = it.addressName,
            placeName = it.placeName,
            categoryName = it.categoryGroupName,
            placeUrl = it.placeUrl,
            brand = brandName,
            x = it.x,
            y = it.y
        )
    }
}