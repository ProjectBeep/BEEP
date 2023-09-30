package com.lighthouse.beep.ui.dialog.textinput

import android.os.Bundle
import androidx.core.os.bundleOf

data class TextInputResult(
    val text: String,
) {
    constructor(bundle: Bundle?): this(getText(bundle))

    companion object {
        const val KEY = "TextInputResult"

        private const val KEY_TEXT = "Key.Text"

        fun getText(bundle: Bundle?): String {
            return bundle?.getString(KEY_TEXT, "") ?: ""
        }
    }

    fun buildBundle(): Bundle {
        return bundleOf(
            KEY_TEXT to text
        )
    }
}