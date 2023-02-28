package com.lighthouse.features.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.ItemButtonBinding
import com.lighthouse.features.setting.model.SettingItem
import com.lighthouse.features.setting.model.SettingMenu

internal class ButtonViewHolder(
    parent: ViewGroup,
    private val onClick: (SettingMenu) -> Unit,
    private val binding: ItemButtonBinding = ItemButtonBinding.bind(
        LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingItem.Button) {
        val context = binding.root.context
        binding.tvLabel.text = item.menu.uiText.asString(context)
        binding.root.setOnClickListener {
            onClick(item.menu)
        }
    }
}
