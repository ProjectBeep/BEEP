package com.lighthouse.beep.ui.feature.login

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class LoginParam private constructor(): AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    companion object {
        fun createParma() = LoginParam()
    }
}