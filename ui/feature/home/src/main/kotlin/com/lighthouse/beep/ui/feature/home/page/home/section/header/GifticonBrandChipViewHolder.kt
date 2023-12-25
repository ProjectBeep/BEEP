package com.lighthouse.beep.ui.feature.home.page.home.section.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredBrandChipBinding
import com.lighthouse.beep.ui.feature.home.model.BrandItem

internal class GifticonBrandChipViewHolder(
    parent: ViewGroup,
    private val onGifticonBrandListener: OnGifticonBrandListener,
    private val onGifticonBrandClickListener: OnGifticonBrandClickListener,
    private val binding: ItemExpiredBrandChipBinding = ItemExpiredBrandChipBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : LifecycleViewHolder<BrandItem>(binding.root) {

    override fun bind(item: BrandItem) {
        super.bind(item)

        binding.chip.text = when(item) {
            is BrandItem.All -> getString(R.string.brand_all)
            is BrandItem.Item -> item.name
        }
    }

    override fun onSetUpClickEvent(item: BrandItem) {
        binding.chip.setOnThrottleClickListener {
            onGifticonBrandClickListener.onClick(item, absoluteAdapterPosition)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: BrandItem) {
        onGifticonBrandListener.getSelectedFlow().collect(lifecycleOwner) { selected ->
            binding.chip.isSelected = item == selected
        }
    }
}