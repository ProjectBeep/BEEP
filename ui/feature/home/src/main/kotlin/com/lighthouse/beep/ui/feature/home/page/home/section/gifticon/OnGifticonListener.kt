package com.lighthouse.beep.ui.feature.home.page.home.section.gifticon

import com.lighthouse.beep.ui.feature.home.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import kotlinx.coroutines.flow.Flow

internal interface OnGifticonListener {

    fun getNextDayEventFlow(): Flow<Unit>

    fun isSelectedFlow(item: HomeItem.GifticonItem): Flow<Boolean>

    fun getViewMode(): Flow<GifticonViewMode>

    fun onClick(item: HomeItem.GifticonItem)
}