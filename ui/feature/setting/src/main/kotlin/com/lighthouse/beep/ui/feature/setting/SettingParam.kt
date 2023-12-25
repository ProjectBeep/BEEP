package com.lighthouse.beep.ui.feature.setting

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class SettingParam: AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, SettingActivity::class.java)
    }
}