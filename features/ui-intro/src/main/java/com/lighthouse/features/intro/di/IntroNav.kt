package com.lighthouse.features.intro.di

import android.content.Context
import com.lighthouse.features.common.navigator.MainNavigator
import javax.inject.Inject

class IntroNav @Inject constructor(
    private val mainNavigator: MainNavigator
) {

    fun openMain(context: Context) = mainNavigator.openMain(context)
}
