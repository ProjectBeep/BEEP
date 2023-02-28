package com.lighthouse.features.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.ItemStateButtonBinding
import com.lighthouse.features.setting.model.SettingItem
import com.lighthouse.features.setting.model.SettingMenu

internal class StateButtonViewHolder(
    parent: ViewGroup,
    private val onClick: (SettingMenu) -> Unit,
    private val binding: ItemStateButtonBinding = ItemStateButtonBinding.bind(
        LayoutInflater.from(parent.context).inflate(R.layout.item_state_button, parent, false)
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingItem.StateButton) {
        val context = binding.root.context
        binding.tvLabel.text = item.menu.uiText.asString(context)
        setUpState(item)
        binding.root.setOnClickListener {
            onClick(item.menu)
        }
    }

    fun bindState(item: SettingItem.StateButton) {
        setUpState(item)
    }

    private fun setUpState(item: SettingItem.StateButton) {
        val context = binding.root.context
        binding.tvState.text = item.state.asString(context)
    }
}
