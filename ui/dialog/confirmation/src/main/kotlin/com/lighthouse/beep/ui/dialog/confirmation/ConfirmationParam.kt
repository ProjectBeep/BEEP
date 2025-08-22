package com.lighthouse.beep.ui.dialog.confirmation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.lighthouse.beep.theme.R as ThemeR

data class ConfirmationParam(
    val message: String = "",
    val messageResId: Int = 0,
    val okText: String = "",
    val okTextResId: Int = 0,
    val okDismiss: Boolean = true,
    val cancelText: String = "",
    val cancelTextResId: Int = 0,
    val cancelDismiss: Boolean = true,
    val windowBackgroundColorResId: Int = ThemeR.color.black_30,
    val windowBackgroundColor: Int = 0
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
            putInt(KEY_WINDOW_BACKGROUND_COLOR_RES_ID, windowBackgroundColorResId)
            putInt(KEY_WINDOW_BACKGROUND_COLOR, windowBackgroundColor)
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
        private const val KEY_WINDOW_BACKGROUND_COLOR_RES_ID = "Key.WindowBackgroundColorResId"
        private const val KEY_WINDOW_BACKGROUND_COLOR = "Key.WindowBackgroundColor"

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

        private fun getColor(
            context: Context,
            arguments: Bundle?,
            keyColor: String,
            keyColorResId: String
        ): Int {
            val color = arguments?.getInt(keyColor)
            if (color != null && color != Color.TRANSPARENT) {
                return color
            }
            val resId = arguments?.getInt(keyColorResId)
            if (resId != null && resId != 0) {
                return context.getColor(resId)
            }
            return Color.TRANSPARENT
        }

        fun getWindowBackgroundColor(context: Context, arguments: Bundle?): Int {
            return getColor(
                context,
                arguments,
                KEY_WINDOW_BACKGROUND_COLOR,
                KEY_WINDOW_BACKGROUND_COLOR_RES_ID
            )
        }
    }
}
