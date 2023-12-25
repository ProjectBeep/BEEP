package com.lighthouse.beep.ui.feature.home.page.home.section.header

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandDiff
import com.lighthouse.beep.ui.feature.home.model.BrandItem

internal class GifticonBrandChipAdapter(
    private val onGifticonBrandListener: OnGifticonBrandListener,
    private val onGifticonBrandClickListener: OnGifticonBrandClickListener,
) : ListAdapter<BrandItem, GifticonBrandChipViewHolder>(ExpiredBrandDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifticonBrandChipViewHolder {
        return GifticonBrandChipViewHolder(parent, onGifticonBrandListener, onGifticonBrandClickListener)
    }

    override fun onBindViewHolder(holder: GifticonBrandChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}