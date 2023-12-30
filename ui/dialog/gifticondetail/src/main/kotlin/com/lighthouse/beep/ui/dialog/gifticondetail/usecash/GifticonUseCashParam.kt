package com.lighthouse.beep.ui.dialog.gifticondetail.usecash

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle

data class GifticonUseCashParam(
    val remainCash: Int,
) {

    companion object {
        private const val KEY_REMAIN_CASH = "Key.RemainCash"

        fun getRemainCash(savedStateHandle: SavedStateHandle?): Int {
            return savedStateHandle?.get<Int>(KEY_REMAIN_CASH) ?: 0
        }
    }

    fun buildBundle(): Bundle = bundleOf(
        KEY_REMAIN_CASH to remainCash
    )
}