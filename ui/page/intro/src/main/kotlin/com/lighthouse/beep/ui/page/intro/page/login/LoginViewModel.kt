package com.lighthouse.beep.ui.page.intro.page.login

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.ui.page.intro.R

class LoginViewModel : ViewModel() {
    val items = listOf(
        LoginData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim1,
        ),
        LoginData(
            titleRes = R.string.recognize_name,
            descriptionRes = R.string.recognize_description,
            lottieRes = R.raw.lottie_anim2,
        ),
        LoginData(
            titleRes = R.string.map_name,
            descriptionRes = R.string.map_description,
            lottieRes = R.raw.lottie_anim3,
        ),
    )
}
