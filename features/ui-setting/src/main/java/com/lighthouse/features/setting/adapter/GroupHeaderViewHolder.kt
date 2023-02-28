package com.lighthouse.features.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.ItemGroupHeaderBinding
import com.lighthouse.features.setting.model.SettingItem

internal class GroupHeaderViewHolder(
    parent: ViewGroup,
    private val binding: ItemGroupHeaderBinding = ItemGroupHeaderBinding.bind(
        LayoutInflater.from(parent.context).inflate(R.layout.item_group_header, parent, false)
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingItem.GroupHeader) {
        val context = binding.root.context
        binding.tvHeaderLabel.text = item.uiText.asString(context)
    }
}
