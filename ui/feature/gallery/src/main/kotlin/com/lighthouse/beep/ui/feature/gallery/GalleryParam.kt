package com.lighthouse.beep.ui.feature.gallery

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.navs.AppNavParam

class GalleryParam private constructor(): AppNavParam {
    companion object {
        fun createParam() = GalleryParam()
    }

    override fun createIntent(context: Context): Intent {
        return Intent(context, GalleryActivity::class.java)
    }
}