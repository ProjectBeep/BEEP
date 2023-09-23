package com.lighthouse.beep.navs

import android.content.Context
import android.content.Intent

interface AppNavigator {

    fun getIntent(context: Context, navItem: ActivityNavItem): Intent
}