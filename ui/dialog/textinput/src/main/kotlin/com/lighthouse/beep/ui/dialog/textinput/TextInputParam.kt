package com.lighthouse.beep.ui.dialog.textinput

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import com.lighthouse.beep.library.textformat.TextInputFormat

data class TextInputParam(
    val text: String = "",
    val hint: String = "",
    val maxLength: Int = Int.MAX_VALUE,
    val inputFormat: TextInputFormat = TextInputFormat.TEXT,
) {
    companion object {
        private const val KEY_TEXT = "Key.Text"
        private const val KEY_HINT = "Key.Hint"
        private const val KEY_MAX_LENGTH = "Key.MaxLength"
        private const val KEY_FORMAT = "Key.Format"

        val None = TextInputParam()

        fun getText(savedStateHandle: SavedStateHandle): String {
            return savedStateHandle.get<String>(KEY_TEXT) ?: ""
        }

        fun getHint(savedStateHandle: SavedStateHandle): String {
            return savedStateHandle.get<String>(KEY_HINT) ?: ""
        }

        fun getMaxLength(savedStateHandle: SavedStateHandle): Int {
            return savedStateHandle.get<Int>(KEY_MAX_LENGTH) ?: Int.MAX_VALUE
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
            KEY_MAX_LENGTH to maxLength,
            KEY_FORMAT to inputFormat.name,
        )
    }
}