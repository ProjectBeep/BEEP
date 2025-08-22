package com.lighthouse.beep.data.local.database.mapper.brand

import com.lighthouse.beep.data.local.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.model.brand.BrandLocation

internal fun List<DBBrandLocationEntity>.toModel(): List<BrandLocation> {
    return map { it.toModel() }
}

internal fun DBBrandLocationEntity.toModel(): BrandLocation {
    return BrandLocation(
        placeUrl = placeUrl,
        addressName = addressName,
        placeName = placeName,
        categoryName = categoryName,
        displayBrand = displayBrand,
        x = x,
        y = y,
    )
}
