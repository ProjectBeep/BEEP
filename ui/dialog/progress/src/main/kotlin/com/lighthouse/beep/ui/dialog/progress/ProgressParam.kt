package com.lighthouse.beep.ui.dialog.progress

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.FloatRange

data class ProgressParam(
    @FloatRange(from = 0.0, to = 1.0) private val dimAmount: Float = DEFAULT_DIM_AMOUNT,
    private val backgroundColor: Int = DEFAULT_BACKGROUND_COLOR,
) {
    fun buildBundle(): Bundle {
        return Bundle().apply {
            putFloat(KEY_DIM_AMOUNT, dimAmount)
            putInt(KEY_BACKGROUND_COLOR, backgroundColor)
        }
    }

    companion object {
        private const val DEFAULT_DIM_AMOUNT = 0.6f
        private const val DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT

        private const val KEY_DIM_AMOUNT = "Key.DimAmount"
        private const val KEY_BACKGROUND_COLOR = "Key.BackgroundColor"

        fun getDimAmount(arguments: Bundle?): Float {
            return arguments?.getFloat(KEY_DIM_AMOUNT) ?: DEFAULT_DIM_AMOUNT
        }

        fun getBackgroundColor(arguments: Bundle?): Int {
            return arguments?.getInt(KEY_BACKGROUND_COLOR) ?: DEFAULT_BACKGROUND_COLOR
        }
    }
}
