package com.lighthouse.beep.ui.feature.login.page.login

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class LoginData(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @RawRes val lottieRes: Int,
)
