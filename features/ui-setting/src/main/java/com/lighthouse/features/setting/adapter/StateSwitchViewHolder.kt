package com.lighthouse.features.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.ItemStateSwitchBinding
import com.lighthouse.features.setting.model.SettingItem
import com.lighthouse.features.setting.model.SettingMenu

internal class StateSwitchViewHolder(
    parent: ViewGroup,
    private val onCheckedChange: (SettingMenu, Boolean) -> Unit,
    private val binding: ItemStateSwitchBinding = ItemStateSwitchBinding.bind(
        LayoutInflater.from(parent.context).inflate(R.layout.item_state_switch, parent, false)
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingItem.StateSwitch) {
        val context = binding.root.context
        binding.tvLabel.text = item.menu.uiText.asString(context)
        setUpState(item)
    }

    fun bindState(item: SettingItem.StateSwitch) {
        setUpState(item)
    }

    private fun setUpState(item: SettingItem.StateSwitch) {
        binding.smState.apply {
            setOnCheckedChangeListener(null)
            isChecked = item.state
            setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(item.menu, isChecked)
            }
        }
    }
}
