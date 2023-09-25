package com.lighthouse.beep.ui.feature.home.decorator

import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredHeaderViewHolder

internal interface HomeItemDecorationCallback {

    fun isShowExpiredHeader(position: Int): Boolean

    fun getHeaderViewHolder(): ExpiredHeaderViewHolder

    fun isExpiredGifticonFirst(position: Int) : Boolean
}