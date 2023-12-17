package com.lighthouse.beep.ui.designsystem.snackbar

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lighthouse.beep.theme.R as ThemeR

@Suppress("unused")
enum class BeepSnackBarState(
    @DrawableRes val iconResId: Int = 0,
    @DrawableRes val backgroundResId: Int = R.drawable.bg_snackbar_default,
    @ColorRes val iconColorResId: Int = ThemeR.color.medium_pink,
    @ColorRes val textColorResId: Int = ThemeR.color.medium_gray,
    @ColorRes val actionColorResId: Int = ThemeR.color.medium_gray,
) {
    NONE,
    INFO(
        iconResId = R.drawable.icon_snack_bar_info,
    ),
    ERROR(
        iconResId = R.drawable.icon_snack_bar_error,
        backgroundResId = R.drawable.bg_snackbar_error,
        iconColorResId = ThemeR.color.white,
        textColorResId = ThemeR.color.white,
        actionColorResId = ThemeR.color.white,
    );

    val isIconVisible = iconResId != 0
}