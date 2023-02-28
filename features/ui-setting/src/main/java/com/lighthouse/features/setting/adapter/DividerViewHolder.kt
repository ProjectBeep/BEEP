package com.lighthouse.features.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.ItemDividerBinding

internal class DividerViewHolder(
    parent: ViewGroup,
    binding: ItemDividerBinding = ItemDividerBinding.bind(
        LayoutInflater.from(parent.context).inflate(R.layout.item_divider, parent, false)
    )
) : RecyclerView.ViewHolder(binding.root)
