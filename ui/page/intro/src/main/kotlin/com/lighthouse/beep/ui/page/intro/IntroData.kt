package com.lighthouse.beep.ui.page.intro

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class IntroData(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @RawRes val lottieRes: Int,
)
