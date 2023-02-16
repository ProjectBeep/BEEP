package com.lighthouse.features.main.navigator

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.lighthouse.features.common.navigator.AddGifticonNavigator
import com.lighthouse.features.common.navigator.HomeNavigator
import com.lighthouse.features.common.navigator.ListNavigator
import com.lighthouse.features.common.navigator.MapNavigator
import com.lighthouse.features.common.navigator.SecurityNavigator
import com.lighthouse.features.common.navigator.SettingNavigator
import javax.inject.Inject

class MainNavigator @Inject constructor(
    private val listNavigator: ListNavigator,
    private val homeNavigator: HomeNavigator,
    private val settingNavigator: SettingNavigator,
    private val mapNavigator: MapNavigator,
    private val addGifticonNavigator: AddGifticonNavigator,
    private val securityNavigator: SecurityNavigator
) {

    fun openList(activity: Activity) = listNavigator.openList(activity)

    fun openHome(activity: Activity) = homeNavigator.openHome(activity)

    fun openSetting(activity: Activity) = settingNavigator.openSetting(activity)

    fun openMap(context: Context) = mapNavigator.openMap(context)

    fun openAddGifticon(
        context: Context,
        result: (ActivityResult) -> Unit
    ) = addGifticonNavigator.openAddGifticon(context, result)

    fun openSecurity(context: Context) = securityNavigator.openSecurity(context)
}
