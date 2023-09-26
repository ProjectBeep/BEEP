package com.lighthouse.beep.navs

import android.content.Context
import com.lighthouse.beep.ui.feature.login.page.login.LoginParam
import android.content.Intent
import com.lighthouse.beep.ui.feature.gallery.GalleryParam
import com.lighthouse.beep.ui.feature.home.HomeParam
import javax.inject.Inject

internal class AppNavigatorImpl @Inject constructor() : AppNavigator {

    override fun getIntent(context: Context, navItem: ActivityNavItem): Intent {
        val param = createParam(navItem)
        return param.createIntent(context)
    }

    private fun createParam(navItem: ActivityNavItem): AppNavParam {
        return when(navItem) {
            is ActivityNavItem.Login -> LoginParam.createParma()
            is ActivityNavItem.Home -> HomeParam.createParma()
            is ActivityNavItem.Gallery -> GalleryParam.createParam()
        }
    }
}