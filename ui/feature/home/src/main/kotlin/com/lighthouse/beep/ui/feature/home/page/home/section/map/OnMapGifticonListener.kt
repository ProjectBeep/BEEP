package com.lighthouse.beep.ui.feature.home.page.home.section.map

import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import kotlinx.coroutines.flow.Flow

internal interface OnMapGifticonListener {

    fun getCurrentDmsPosFlow(): Flow<DmsPos>

    fun onClick(item: MapGifticonItem)
}