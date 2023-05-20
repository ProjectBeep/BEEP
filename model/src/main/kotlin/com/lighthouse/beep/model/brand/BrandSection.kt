package com.lighthouse.beep.model.brand

import com.lighthouse.beep.model.location.Dms
import com.lighthouse.beep.model.location.DmsPos

data class BrandSection(
    val id: Long,
    val brand: String,
    val x: Dms,
    val y: Dms,
) {
    val pos = DmsPos(x, y)
}
