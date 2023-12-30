package com.lighthouse.beep.ui.feature.archive.list

import com.lighthouse.beep.ui.feature.archive.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.archive.model.UsedGifticonItem
import kotlinx.coroutines.flow.Flow

internal interface UsedGifticonListener {

    fun isSelectedFlow(item: UsedGifticonItem): Flow<Boolean>

    fun getViewModeFlow(): Flow<GifticonViewMode>

    fun onClick(item: UsedGifticonItem)
}