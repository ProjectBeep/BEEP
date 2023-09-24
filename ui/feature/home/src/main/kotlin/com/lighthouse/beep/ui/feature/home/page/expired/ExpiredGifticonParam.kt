package com.lighthouse.beep.ui.feature.home.page.expired

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import com.lighthouse.beep.ui.feature.home.R

internal class ExpiredGifticonParam(
    private val order: ExpiredOrder,
) {

    fun buildBundle(): Bundle = Bundle().apply {
        putString(KEY_EXPIRED_ORDER, order.name)
    }

    companion object {
        private const val KEY_EXPIRED_ORDER = "Key.ExpiredOrder"

        fun getExpiredOrder(savedStateHandle: SavedStateHandle): ExpiredOrder {
            val value = savedStateHandle.get<String>(KEY_EXPIRED_ORDER) ?: ExpiredOrder.D_DAY.name
            return runCatching {
                ExpiredOrder.valueOf(value)
            }.getOrDefault(ExpiredOrder.D_DAY)
        }
    }
}

internal enum class ExpiredOrder(
    @StringRes val titleRes: Int,
) {

    D_DAY(R.string.dday_order),
    RECENT(R.string.recent_order),
}