package com.lighthouse.beep.ui.page.intro

import androidx.lifecycle.ViewModel

class IntroViewModel : ViewModel() {
    val items = listOf(
        IntroData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim1,
        ),
        IntroData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim2,
        ),
        IntroData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim3,
        ),
    )
}
