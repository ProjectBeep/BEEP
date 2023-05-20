package com.lighthouse.beep.data.database.mapper.brand

import com.lighthouse.beep.data.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.model.brand.BrandSection

internal fun DBBrandSectionEntity.toModel(): BrandSection? {
    id ?: return null
    return BrandSection(
        id = id,
        brand = brand,
        x = x,
        y = y,
    )
}
