package com.lighthouse.beep.ui.feature.home.page.home.section.expired

import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem

internal fun interface OnExpiredBrandClickListener {
    fun onClick(item: ExpiredBrandItem, position: Int)
}