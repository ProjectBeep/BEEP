package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandDiff
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem

internal class ExpiredBrandChipAdapter(
    private val onExpiredBrandListener: OnExpiredBrandListener,
) : ListAdapter<ExpiredBrandItem, ExpiredBrandChipViewHolder>(ExpiredBrandDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiredBrandChipViewHolder {
        return ExpiredBrandChipViewHolder(parent, onExpiredBrandListener)
    }

    override fun onBindViewHolder(holder: ExpiredBrandChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}