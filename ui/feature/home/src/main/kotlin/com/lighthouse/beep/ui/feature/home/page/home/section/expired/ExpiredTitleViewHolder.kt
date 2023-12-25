package com.lighthouse.beep.ui.feature.home.page.home.section.expired

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredTitleBinding

internal class ExpiredTitleViewHolder(
    parent: ViewGroup,
    private val binding: ItemExpiredTitleBinding = ItemExpiredTitleBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): RecyclerView.ViewHolder(binding.root)