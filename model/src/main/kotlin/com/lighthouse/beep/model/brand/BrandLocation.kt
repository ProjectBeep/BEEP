package com.lighthouse.beep.model.brand

import com.lighthouse.beep.model.location.Dms

data class BrandLocation(
    val placeUrl: String,
    val addressName: String,
    val placeName: String,
    val categoryName: String,
    val displayBrand: String,
    val x: Dms,
    val y: Dms,
)
