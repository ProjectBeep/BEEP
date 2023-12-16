package com.lighthouse.beep.ui.dialog.confirmation

import android.content.Context
import android.os.Bundle

data class ConfirmationParam(
    val message: String = "",
    val messageResId: Int = 0,
    val okText: String = "",
    val okTextResId: Int = 0,
    val okDismiss: Boolean = true,
    val cancelText: String = "",
    val cancelTextResId: Int = 0,
    val cancelDismiss: Boolean = true,
) {

    fun buildBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_MESSAGE, message)
            putInt(KEY_MESSAGE_RES_ID, messageResId)
            putString(KEY_OK_TEXT, okText)
            putInt(KEY_OK_TEXT_RES_ID, okTextResId)
            putBoolean(KEY_OK_DISMISS, okDismiss)
            putString(KEY_CANCEL_TEXT, cancelText)
            putInt(KEY_CANCEL_TEXT_RES_ID, cancelTextResId)
            putBoolean(KEY_CANCEL_DISMISS, cancelDismiss)
        }
    }

    companion object {
        private const val KEY_MESSAGE = "Key.Message"
        private const val KEY_MESSAGE_RES_ID = "Key.MessageResId"
        private const val KEY_OK_TEXT = "Key.OkText"
        private const val KEY_OK_TEXT_RES_ID = "Key.OkTextResId"
        private const val KEY_OK_DISMISS = "Key.OkDismiss"
        private const val KEY_CANCEL_TEXT = "Key.CancelText"
        private const val KEY_CANCEL_TEXT_RES_ID = "Key.CancelTextResId"
        private const val KEY_CANCEL_DISMISS = "Key.CancelDismiss"

        private fun getText(
            context: Context,
            arguments: Bundle?,
            keyText: String,
            keyResId: String,
        ): String {
            val text = arguments?.getString(keyText)
            if (!text.isNullOrEmpty()) {
                return text
            }
            val resId = arguments?.getInt(keyResId)
            if (resId != null && resId != 0) {
                return context.getString(resId)
            }
            return ""
        }

        fun getMessage(context: Context, arguments: Bundle?): String {
            return getText(context, arguments, KEY_MESSAGE, KEY_MESSAGE_RES_ID)
        }

        fun getOkText(context: Context, arguments: Bundle?): String {
            return getText(context, arguments, KEY_OK_TEXT, KEY_OK_TEXT_RES_ID)
        }

        fun isOkDismiss(arguments: Bundle?): Boolean {
            return arguments?.getBoolean(KEY_OK_DISMISS) ?: true
        }

        fun getCancelText(context: Context, arguments: Bundle?): String {
            return getText(context, arguments, KEY_CANCEL_TEXT, KEY_CANCEL_TEXT_RES_ID)
        }

        fun isCancelDismiss(arguments: Bundle?): Boolean {
            return arguments?.getBoolean(KEY_CANCEL_DISMISS) ?: true
        }
    }
}
