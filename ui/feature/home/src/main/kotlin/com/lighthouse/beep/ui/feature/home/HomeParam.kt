package com.lighthouse.beep.ui.feature.home

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class HomeParam: AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, HomeActivity::class.java)
    }
}