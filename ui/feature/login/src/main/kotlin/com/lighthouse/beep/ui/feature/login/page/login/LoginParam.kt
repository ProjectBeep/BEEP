package com.lighthouse.beep.ui.feature.login.page.login

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class LoginParam: AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, LoginActivity::class.java)
    }
}