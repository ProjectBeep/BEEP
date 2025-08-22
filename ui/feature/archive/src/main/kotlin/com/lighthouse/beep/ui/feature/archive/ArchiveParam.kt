package com.lighthouse.beep.ui.feature.archive

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class ArchiveParam : AppNavParam {

    override fun createIntent(context: Context): Intent {
        return Intent(context, ArchiveActivity::class.java)
    }
}