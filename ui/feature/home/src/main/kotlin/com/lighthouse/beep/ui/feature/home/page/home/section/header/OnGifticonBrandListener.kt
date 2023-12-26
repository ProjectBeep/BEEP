package com.lighthouse.beep.ui.feature.home.page.home.section.header

import com.lighthouse.beep.ui.feature.home.model.BrandItem
import kotlinx.coroutines.flow.Flow

internal interface OnGifticonBrandListener {

    fun getSelectedFlow(item: BrandItem): Flow<Boolean>
}