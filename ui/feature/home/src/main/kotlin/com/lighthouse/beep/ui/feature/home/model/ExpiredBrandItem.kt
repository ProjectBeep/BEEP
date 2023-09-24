package com.lighthouse.beep.ui.feature.home.model

import androidx.recyclerview.widget.DiffUtil

internal sealed interface ExpiredBrandItem {

    data object All : ExpiredBrandItem

    data class Item(val name: String) : ExpiredBrandItem
}

internal class ExpiredBrandDiff : DiffUtil.ItemCallback<ExpiredBrandItem>() {
    override fun areItemsTheSame(oldItem: ExpiredBrandItem, newItem: ExpiredBrandItem): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem is ExpiredBrandItem.Item &&
                    newItem is ExpiredBrandItem.Item &&
                    oldItem.name == newItem.name -> true

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: ExpiredBrandItem, newItem: ExpiredBrandItem): Boolean {
        return oldItem == newItem
    }
}