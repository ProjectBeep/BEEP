package com.lighthouse.beep.ui.feature.home.model

import androidx.annotation.StringRes
import com.lighthouse.beep.ui.feature.home.R

internal enum class ExpiredOrder(
    @StringRes val titleRes: Int,
) {

    D_DAY(R.string.dday_order),
    RECENT(R.string.recent_order),
}