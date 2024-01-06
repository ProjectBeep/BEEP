package com.lighthouse.beep.ui.feature.editor.page.edit

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import com.lighthouse.beep.navs.AppNavParam

class EditParam(
    private val gifticonId: Long = -1,
): AppNavParam {

    companion object {
        private const val KEY_GIFTICON_ID = "Key.GifticonId"

        fun getGifticonId(savedStateHandle: SavedStateHandle): Long {
            return savedStateHandle.get<Long>(KEY_GIFTICON_ID) ?: -1
        }
    }

    override fun createIntent(context: Context): Intent {
        return Intent(context, EditActivity::class.java).apply {
            putExtra(KEY_GIFTICON_ID, gifticonId)
        }
    }
}