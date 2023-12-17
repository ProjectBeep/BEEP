package com.lighthouse.beep.ui.designsystem.snackbar

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lighthouse.beep.core.ui.exts.getString

sealed class BeepSnackBarAction(
    val listener: OnSnackBarActionListener?
) {

    data object None : BeepSnackBarAction(null)

    class Icon(
        @DrawableRes val drawableResId: Int,
        listener: OnSnackBarActionListener,
    ): BeepSnackBarAction(listener)

    class Text(
        private val text: String = "",
        @StringRes
        private val textResId: Int = 0,
        listener: OnSnackBarActionListener,
    ): BeepSnackBarAction(listener) {

        fun getText(context: Context): String {
            return context.getString(text, textResId)
        }
    }
}