package com.lighthouse.beep.ui.feature.home.page.home

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class HomeParam private constructor(): AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, HomeActivity::class.java)
    }

    companion object {
        fun createParma() = HomeParam()
    }
}