package com.lighthouse.beep.ui.feature.home.decorator

import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredHeaderViewHolder

internal interface HomeItemDecorationCallback {

    fun onTopItemPosition(position: Int)

    fun getExpiredGifticonFirstIndex(): Int

    fun getHeaderViewHolder(): ExpiredHeaderViewHolder
}