package com.lighthouse.beep.ui.feature.home.adapter.expired

import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem

internal fun interface OnExpiredBrandClickListener {
    fun onClick(item: ExpiredBrandItem, position: Int)
}