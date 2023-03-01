package com.lighthouse.features.setting.adapter

import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.features.setting.model.SettingItem

internal class SettingItemDiff : DiffUtil.ItemCallback<SettingItem>() {

    override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        if (oldItem == newItem) return true
        if (oldItem is SettingItem.Button &&
            newItem is SettingItem.Button &&
            oldItem.menu == newItem.menu
        ) {
            return true
        } else if (
            oldItem is SettingItem.StateButton &&
            newItem is SettingItem.StateButton &&
            oldItem.menu == newItem.menu
        ) {
            return true
        } else if (
            oldItem is SettingItem.StateSwitch &&
            newItem is SettingItem.StateSwitch &&
            oldItem.menu == newItem.menu
        ) {
            return true
        }

        return false
    }

    override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: SettingItem, newItem: SettingItem): Any? {
        if (oldItem is SettingItem.StateButton &&
            newItem is SettingItem.StateButton &&
            oldItem.state != newItem.state
        ) {
            return UPDATE_STATE
        } else if (
            oldItem is SettingItem.StateSwitch &&
            newItem is SettingItem.StateSwitch &&
            oldItem.state != newItem.state
        ) {
            return UPDATE_STATE
        }
        return null
    }

    companion object {
        const val UPDATE_STATE = 1 shl 0
    }
}
