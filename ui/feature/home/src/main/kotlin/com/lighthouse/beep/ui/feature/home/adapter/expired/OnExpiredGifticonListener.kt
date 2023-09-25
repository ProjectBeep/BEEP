package com.lighthouse.beep.ui.feature.home.adapter.expired

import com.lighthouse.beep.ui.feature.home.model.HomeItem
import kotlinx.coroutines.flow.Flow

internal interface OnExpiredGifticonListener {

    fun getNextDayEventFlow(): Flow<Unit>

    fun onClick(item: HomeItem.ExpiredGifticonItem)
}