package com.lighthouse.beep.data.local.database.mapper.brand

import com.lighthouse.beep.data.local.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.model.brand.BrandLocation

internal fun BrandLocation.toEntity(sectionId: Long): DBBrandLocationEntity {
    return DBBrandLocationEntity(
        id = null,
        sectionId = sectionId,
        placeUrl = placeUrl,
        placeName = placeName,
        addressName = addressName,
        categoryName = categoryName,
        brand = displayBrand.lowercase(),
        displayBrand = displayBrand,
        x = x,
        y = y,
        createdAt = java.util.Date()
    )
}
