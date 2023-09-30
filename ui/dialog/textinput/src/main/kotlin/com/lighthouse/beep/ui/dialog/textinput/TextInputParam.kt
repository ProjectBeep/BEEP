package com.lighthouse.beep.ui.dialog.textinput

import android.os.Bundle
import androidx.core.os.bundleOf

data class TextInputParam(
    val text: String = "",
    val hint: String = "",
) {
    companion object {
        private const val KEY_TEXT = "Key.Text"
        private const val KEY_HINT = "Key.Hint"

        fun getText(arguments: Bundle?): String {
            return arguments?.getString(KEY_TEXT) ?: ""
        }

        fun getHint(arguments: Bundle?): String {
            return arguments?.getString(KEY_HINT) ?: ""
        }
    }

    fun buildBundle(): Bundle {
        return bundleOf(
            KEY_TEXT to text,
            KEY_HINT to hint
        )
    }
}