package com.lighthouse.beep.ui.feature.home.page.home.section.map

import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import kotlinx.coroutines.flow.Flow

internal interface OnMapGifticonSectionListener: OnMapGifticonListener {

    fun getMapGifticonListFlow(): Flow<List<MapGifticonItem>>

    fun onGotoMapClick()
}