package com.lighthouse.beep.ui.dialog.progress

import android.graphics.Color
import android.os.Bundle

data class ProgressParam(
    private val backgroundColor: Int = DEFAULT_BACKGROUND_COLOR,
) {
    fun buildBundle(): Bundle {
        return Bundle().apply {
            putInt(KEY_BACKGROUND_COLOR, backgroundColor)
        }
    }

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT

        private const val KEY_BACKGROUND_COLOR = "Key.BackgroundColor"

        fun getBackgroundColor(arguments: Bundle?): Int {
            return arguments?.getInt(KEY_BACKGROUND_COLOR) ?: DEFAULT_BACKGROUND_COLOR
        }
    }
}
