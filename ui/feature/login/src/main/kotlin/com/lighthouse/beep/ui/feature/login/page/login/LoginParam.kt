package com.lighthouse.beep.ui.feature.login.page.login

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

data class LoginParam(
    private val clearTask: Boolean
): AppNavParam {

    override fun createIntent(context: Context): Intent {
        val intent = Intent(context, LoginActivity::class.java)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return intent
    }
}