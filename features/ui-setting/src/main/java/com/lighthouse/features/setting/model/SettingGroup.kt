package com.lighthouse.features.setting.model

import com.lighthouse.core.android.utils.resource.UIText

internal class SettingGroup(val title: UIText?, val items: List<SettingItem>) {

    class Builder {
        private var title: UIText? = null
        private val items = ArrayList<SettingItem>()

        fun setTitle(title: UIText?) = apply {
            this.title = title
        }

        fun addItem(item: SettingItem) = apply {
            items.add(item)
        }

        fun addButton(menu: SettingMenu) = apply {
            addItem(SettingItem.Button(menu))
        }

        fun addStateButton(menu: SettingMenu, state: UIText = UIText.Empty) = apply {
            addItem(SettingItem.StateButton(menu, state))
        }

        fun addStateSwitch(menu: SettingMenu, state: Boolean = false) = apply {
            addItem(SettingItem.StateSwitch(menu, state))
        }

        fun build(): SettingGroup {
            return SettingGroup(title, items.toList())
        }
    }
}
