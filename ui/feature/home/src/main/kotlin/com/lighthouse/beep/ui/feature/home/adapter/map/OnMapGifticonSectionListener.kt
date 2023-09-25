package com.lighthouse.beep.ui.feature.home.adapter.map

import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import kotlinx.coroutines.flow.Flow

internal interface OnMapGifticonSectionListener {

    fun getMapGifticonListFlow(): Flow<List<MapGifticonItem>>

    fun onGotoMapClick()
}