package com.lighthouse.beep.data.remote.mapper

import com.lighthouse.beep.data.remote.model.BrandPlaceInfoDataContainer
import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.Dms

internal fun BrandPlaceInfoDataContainer.toBrandPlaceInfoList(
    brandName: String,
): List<BrandLocation> {
    return documents.map {
        BrandLocation(
            addressName = it.addressName,
            placeName = it.placeName,
            categoryName = it.categoryGroupName,
            placeUrl = it.placeUrl,
            displayBrand = brandName,
            x = Dms(it.x.toDouble()),
            y = Dms(it.y.toDouble()),
        )
    }
}
