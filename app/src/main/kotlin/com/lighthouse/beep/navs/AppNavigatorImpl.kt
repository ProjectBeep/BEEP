package com.lighthouse.beep.navs

import android.content.Context
import com.lighthouse.beep.ui.feature.login.page.login.LoginParam
import android.content.Intent
import com.lighthouse.beep.ui.feature.archive.ArchiveParam
import com.lighthouse.beep.ui.feature.editor.page.edit.EditParam
import com.lighthouse.beep.ui.feature.editor.page.register.RegisterParam
import com.lighthouse.beep.ui.feature.gallery.GalleryParam
import com.lighthouse.beep.ui.feature.home.HomeParam
import com.lighthouse.beep.ui.feature.setting.SettingParam
import javax.inject.Inject

internal class AppNavigatorImpl @Inject constructor() : AppNavigator {

    override fun getIntent(context: Context, navItem: ActivityNavItem): Intent {
        val param = createParam(navItem)
        return param.createIntent(context)
    }

    private fun createParam(navItem: ActivityNavItem): AppNavParam {
        return when(navItem) {
            is ActivityNavItem.Login -> LoginParam()
            is ActivityNavItem.Home -> HomeParam()
            is ActivityNavItem.Gallery -> GalleryParam()
            is ActivityNavItem.Register -> RegisterParam(navItem.list)
            is ActivityNavItem.Edit -> EditParam(navItem.gifticonId)
            is ActivityNavItem.Archive -> ArchiveParam()
            is ActivityNavItem.Setting -> SettingParam()
        }
    }
}