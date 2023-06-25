package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBBrandCategory
import com.lighthouse.beep.model.brand.BrandCategory

internal fun List<DBBrandCategory>.toModel(): List<BrandCategory> {
    return map {
        it.toModel()
    }
}

internal fun DBBrandCategory.toModel(): BrandCategory {
    return BrandCategory(displayBrand, count)
}
