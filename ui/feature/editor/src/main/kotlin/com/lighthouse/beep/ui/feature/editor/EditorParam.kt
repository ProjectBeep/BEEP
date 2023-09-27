package com.lighthouse.beep.ui.feature.editor

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class EditorParam private constructor(): AppNavParam {

    companion object {
        fun createParam() = EditorParam()
    }

    override fun createIntent(context: Context): Intent {
        return Intent(context, EditorActivity::class.java)
    }
}