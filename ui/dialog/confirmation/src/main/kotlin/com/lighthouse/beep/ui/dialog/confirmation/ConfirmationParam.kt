package com.lighthouse.beep.ui.dialog.confirmation

import android.os.Bundle

data class ConfirmationParam(
    val message: String = "",
    val okText: String = "",
    val cancelText: String = "",
) {

    fun buildBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_MESSAGE, message)
            putString(KEY_OK_TEXT, okText)
            putString(KEY_CANCEL_TEXT, cancelText)
        }
    }

    companion object {
        private const val KEY_MESSAGE = "Key.Message"
        private const val KEY_OK_TEXT = "Key.OkText"
        private const val KEY_CANCEL_TEXT = "Key.CancelText"

        fun getMessage(arguments: Bundle?): String {
            return arguments?.getString(KEY_MESSAGE) ?: ""
        }

        fun getOkText(arguments: Bundle?): String {
            return arguments?.getString(KEY_OK_TEXT) ?: ""
        }

        fun getCancelText(arguments: Bundle?): String {
            return arguments?.getString(KEY_CANCEL_TEXT) ?: ""
        }
    }
}
