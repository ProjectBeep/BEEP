package com.lighthouse.beep.ui.feature.gallery

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.navs.AppNavParam

class GalleryParam private constructor(
    val list: List<GalleryImage> = emptyList()
): AppNavParam {

    companion object {
        fun createParam(list: List<GalleryImage> = emptyList()) = GalleryParam(list)


    }

    override fun createIntent(context: Context): Intent {
        return Intent(context, GalleryActivity::class.java)
    }
}