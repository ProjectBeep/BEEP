package com.lighthouse.navigator

import android.content.Context
import android.content.Intent
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.main.ui.MainContainerFragment
import javax.inject.Inject

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {

    override fun openMain(context: Context) {
        val intent = Intent(context, MainContainerFragment::class.java)
        context.startActivity(intent)
    }
}
