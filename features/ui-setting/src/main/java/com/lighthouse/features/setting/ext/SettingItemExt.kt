package com.lighthouse.features.setting.ext

import com.lighthouse.features.setting.model.SettingItem

internal fun settingItems(block: SettingItem.Builder.() -> Unit): List<SettingItem> {
    val builder = SettingItem.Builder()
    builder.block()
    return builder.build()
}
