package com.lighthouse.features.setting.ext

import com.lighthouse.features.setting.model.SettingGroup

internal fun settingGroup(block: SettingGroup.Builder.() -> Unit): SettingGroup {
    val builder = SettingGroup.Builder()
    builder.block()
    return builder.build()
}
