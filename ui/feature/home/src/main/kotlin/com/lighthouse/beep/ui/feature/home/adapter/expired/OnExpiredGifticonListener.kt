package com.lighthouse.beep.ui.feature.home.adapter.expired

import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonItem
import kotlinx.coroutines.flow.Flow

internal interface OnExpiredGifticonListener {

    fun getNextDayEventFlow(): Flow<Unit>

    fun onClick(item: ExpiredGifticonItem)
}