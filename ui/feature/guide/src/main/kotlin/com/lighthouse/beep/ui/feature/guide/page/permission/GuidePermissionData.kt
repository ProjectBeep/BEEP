package com.lighthouse.beep.ui.feature.guide.page.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class GuidePermissionData(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
)
