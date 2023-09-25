package com.lighthouse.beep.ui.feature.home.adapter.expired

import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import kotlinx.coroutines.flow.Flow

internal interface OnExpiredHeaderListener {

    fun getSelectedOrder(): Flow<ExpiredOrder>

    fun onOrderClick(order: ExpiredOrder)

    fun onBrandClick(item: ExpiredBrandItem)

    fun onGotoEditClick()
}