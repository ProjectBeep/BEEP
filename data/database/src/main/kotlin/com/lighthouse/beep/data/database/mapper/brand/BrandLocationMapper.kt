package com.lighthouse.beep.data.database.mapper.brand

import com.lighthouse.beep.data.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.model.brand.BrandLocation

internal fun BrandLocation.toEntity(sectionId: Long): DBBrandLocationEntity {
    return DBBrandLocationEntity(
        placeUrl = placeUrl,
        sectionId = sectionId,
        addressName = addressName,
        placeName = placeName,
        categoryName = categoryName,
        displayBrand = displayBrand,
        brand = displayBrand.lowercase(),
        x = x,
        y = y,
    )
}
