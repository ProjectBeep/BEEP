package com.lighthouse.beep.navs.result

import android.content.Intent

interface AppNavResult {
    fun createIntent(): Intent
}