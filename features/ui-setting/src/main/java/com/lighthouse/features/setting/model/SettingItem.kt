package com.lighthouse.features.setting.model

import com.lighthouse.core.android.utils.resource.UIText

internal sealed class SettingItem {

    data class GroupHeader(val uiText: UIText) : SettingItem()

    object Divider : SettingItem()

    data class Button(val menu: SettingMenu) : SettingItem()

    data class StateButton(val menu: SettingMenu, val state: UIText) : SettingItem()

    data class StateSwitch(val menu: SettingMenu, val state: Boolean) : SettingItem()

    class Builder {
        private val groups = ArrayList<SettingGroup>()

        fun addGroup(group: SettingGroup) = apply {
            groups.add(group)
        }

        fun addItem(item: SettingItem) = apply {
            addGroup(
                SettingGroup.Builder()
                    .addItem(item)
                    .build()
            )
        }

        fun addButton(menu: SettingMenu) = apply {
            addItem(Button(menu))
        }

        fun addStateButton(menu: SettingMenu, state: UIText = UIText.Empty) = apply {
            addItem(StateButton(menu, state))
        }

        fun addStateSwitch(menu: SettingMenu, state: Boolean = false) = apply {
            addItem(StateSwitch(menu, state))
        }

        fun build(): List<SettingItem> {
            val list = ArrayList<SettingItem>()

            for (group in groups) {
                if (group.title != null) {
                    list.add(GroupHeader(group.title))
                }

                group.items.forEachIndexed { index, item ->
                    list.add(item)
                    if (index < group.items.lastIndex) {
                        list.add(Divider)
                    }
                }
            }

            return list.toList()
        }
    }
}
