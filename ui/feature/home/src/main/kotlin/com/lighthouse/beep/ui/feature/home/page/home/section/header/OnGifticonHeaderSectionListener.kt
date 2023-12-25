package com.lighthouse.beep.ui.feature.home.page.home.section.header

import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.ui.feature.home.model.BrandItem
import com.lighthouse.beep.ui.feature.home.model.GifticonOrder
import kotlinx.coroutines.flow.Flow

internal interface OnGifticonHeaderSectionListener: OnGifticonBrandListener {

    fun getSelectedOrder(): Flow<GifticonOrder>

    fun getBrandListFlow(): Flow<List<BrandItem>>

    fun getBrandScrollInfo(): Flow<ScrollInfo>

    fun onOrderClick(order: GifticonOrder)

    fun onBrandClick(item: BrandItem)

    fun onGotoEditClick()

    fun onBrandScroll(scrollInfo: ScrollInfo)
}