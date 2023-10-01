package com.lighthouse.beep.ui.dialog.textinput

import android.os.Bundle
import androidx.core.os.bundleOf

data class TextInputResult(
    val displayText: String,
    val value: String,
) {
    constructor(bundle: Bundle?): this(
        getDisplayText(bundle),
        getValue(bundle),
    )

    companion object {
        const val KEY = "TextInputResult"

        private const val KEY_DISPLAY_TEXT = "Key.DisplayText"
        private const val KEY_VALUE = "Key.Value"

        fun getDisplayText(bundle: Bundle?): String {
            return bundle?.getString(KEY_DISPLAY_TEXT, "") ?: ""
        }

        fun getValue(bundle: Bundle?): String {
            return bundle?.getString(KEY_VALUE, "") ?: ""
        }
    }

    fun buildBundle(): Bundle {
        return bundleOf(
            KEY_DISPLAY_TEXT to displayText,
            KEY_VALUE to value,
        )
    }
}