package com.lighthouse.features.setting.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.model.SettingItem
import com.lighthouse.features.setting.model.SettingMenu

internal class SettingAdapter(
    private val onClick: (SettingMenu) -> Unit,
    private val onCheckedChange: (SettingMenu, Boolean) -> Unit
) : ListAdapter<SettingItem, RecyclerView.ViewHolder>(SettingItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_GROUP_HEADER -> GroupHeaderViewHolder(parent)
            TYPE_DIVIDER -> DividerViewHolder(parent)
            TYPE_BUTTON -> ButtonViewHolder(parent, onClick)
            TYPE_STATE_BUTTON -> StateButtonViewHolder(parent, onClick)
            TYPE_STATE_SWITCH -> StateSwitchViewHolder(parent, onCheckedChange)
            else -> throw IllegalArgumentException("잘못된 viewType 입니다.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is GroupHeaderViewHolder && item is SettingItem.GroupHeader -> holder.bind(item)
            holder is ButtonViewHolder && item is SettingItem.Button -> holder.bind(item)
            holder is StateButtonViewHolder && item is SettingItem.StateButton -> holder.bind(item)
            holder is StateSwitchViewHolder && item is SettingItem.StateSwitch -> holder.bind(item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val flag = payloads.getOrNull(0) as? Int ?: 0
            val item = getItem(position)
            when {
                holder is StateButtonViewHolder && item is SettingItem.StateButton &&
                    flag and SettingItemDiff.UPDATE_STATE == SettingItemDiff.UPDATE_STATE -> {
                    holder.bindState(item)
                }

                holder is StateSwitchViewHolder && item is SettingItem.StateSwitch &&
                    flag and SettingItemDiff.UPDATE_STATE == SettingItemDiff.UPDATE_STATE -> {
                    holder.bindState(item)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SettingItem.GroupHeader -> TYPE_GROUP_HEADER
            is SettingItem.Divider -> TYPE_DIVIDER
            is SettingItem.Button -> TYPE_BUTTON
            is SettingItem.StateButton -> TYPE_STATE_BUTTON
            is SettingItem.StateSwitch -> TYPE_STATE_SWITCH
        }
    }

    companion object {
        private const val TYPE_GROUP_HEADER = 1
        private const val TYPE_DIVIDER = 2
        private const val TYPE_BUTTON = 3
        private const val TYPE_STATE_BUTTON = 4
        private const val TYPE_STATE_SWITCH = 5
    }
}
