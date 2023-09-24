package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonDiff
import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonItem

internal class ExpiredGifticonAdapter(
    private val onExpiredGifticonListener: OnExpiredGifticonListener,
) : ListAdapter<ExpiredGifticonItem, ExpiredGifticonViewHolder>(ExpiredGifticonDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiredGifticonViewHolder {
        return ExpiredGifticonViewHolder(parent, onExpiredGifticonListener)
    }

    override fun onBindViewHolder(holder: ExpiredGifticonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}