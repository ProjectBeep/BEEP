package com.lighthouse.beep.ui.dialog.textinput

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle

data class TextInputParam(
    val text: String = "",
    val hint: String = "",
    val inputFormat: TextInputFormat = TextInputFormat.TEXT,
) {
    companion object {
        private const val KEY_TEXT = "Key.Text"
        private const val KEY_HINT = "Key.Hint"
        private const val KEY_FORMAT = "Key.Format"

        fun getText(savedStateHandle: SavedStateHandle): String {
            return savedStateHandle.get<String>(KEY_TEXT) ?: ""
        }

        fun getHint(savedStateHandle: SavedStateHandle): String {
            return savedStateHandle.get<String>(KEY_HINT) ?: ""
        }

        fun getInputFormat(savedStateHandle: SavedStateHandle): TextInputFormat {
            val value = savedStateHandle.get<String>(KEY_FORMAT) ?: ""
            return runCatching {
                TextInputFormat.valueOf(value)
            }.getOrDefault(TextInputFormat.TEXT)
        }
    }

    fun buildBundle(): Bundle {
        return bundleOf(
            KEY_TEXT to text,
            KEY_HINT to hint,
            KEY_FORMAT to inputFormat.name,
        )
    }
}