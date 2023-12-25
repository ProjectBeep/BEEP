package com.lighthouse.beep.ui.feature.home.model

import androidx.recyclerview.widget.DiffUtil

internal sealed interface BrandItem {

    data object All : BrandItem

    data class Item(val name: String) : BrandItem
}

internal class ExpiredBrandDiff : DiffUtil.ItemCallback<BrandItem>() {
    override fun areItemsTheSame(oldItem: BrandItem, newItem: BrandItem): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem is BrandItem.Item &&
                    newItem is BrandItem.Item &&
                    oldItem.name == newItem.name -> true

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: BrandItem, newItem: BrandItem): Boolean {
        return oldItem == newItem
    }
}