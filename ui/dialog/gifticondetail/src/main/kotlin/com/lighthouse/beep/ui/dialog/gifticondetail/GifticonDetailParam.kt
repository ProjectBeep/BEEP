package com.lighthouse.beep.ui.dialog.gifticondetail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle

data class GifticonDetailParam(
    val gifticonId: Long
) {
    companion object {
        private const val KEY_GIFTICON_ID = "Key.GifticonId"

        fun getGifticonId(savedStateHandle: SavedStateHandle): Long {
            return savedStateHandle.get<Long>(KEY_GIFTICON_ID) ?: -1L
        }
    }

    fun buildBundle(): Bundle = bundleOf(KEY_GIFTICON_ID to gifticonId)
}