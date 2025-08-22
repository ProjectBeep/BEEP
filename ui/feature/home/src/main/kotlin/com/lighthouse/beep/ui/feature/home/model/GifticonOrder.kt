package com.lighthouse.beep.ui.feature.home.model

import androidx.annotation.StringRes
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.ui.feature.home.R

internal enum class GifticonOrder(
    @StringRes val titleRes: Int,
    val sortBy: GifticonSortBy,
) {

    D_DAY(R.string.dday_order, GifticonSortBy.DEADLINE),
    RECENT(R.string.recent_order, GifticonSortBy.RECENT),
}