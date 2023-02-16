package com.lighthouse.navigator

import android.content.Context
import android.content.Intent
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.main.ui.MainActivity
import javax.inject.Inject

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {

    override fun openMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
