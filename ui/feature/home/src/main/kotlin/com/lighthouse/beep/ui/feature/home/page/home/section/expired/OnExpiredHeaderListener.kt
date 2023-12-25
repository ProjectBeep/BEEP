package com.lighthouse.beep.ui.feature.home.page.home.section.expired

import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import kotlinx.coroutines.flow.Flow

internal interface OnExpiredHeaderListener {

    fun getSelectedOrder(): Flow<ExpiredOrder>

    fun getBrandListFlow(): Flow<List<ExpiredBrandItem>>

    fun getBrandScrollInfo(): Flow<ScrollInfo>

    fun onOrderClick(order: ExpiredOrder)

    fun onBrandClick(item: ExpiredBrandItem)

    fun onGotoEditClick()

    fun onBrandScroll(scrollInfo: ScrollInfo)
}