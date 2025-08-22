package com.lighthouse.beep.ui.feature.home.page.home.section.header

import com.lighthouse.beep.ui.feature.home.model.BrandItem

internal fun interface OnGifticonBrandClickListener {
    fun onClick(item: BrandItem, position: Int)
}