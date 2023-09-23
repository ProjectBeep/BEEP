package com.lighthouse.beep.navs

import android.content.Context
import android.content.Intent

interface AppNavParam {

    fun createIntent(context: Context): Intent
}