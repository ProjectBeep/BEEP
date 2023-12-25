package com.lighthouse.beep.ui.feature.home.page.home.section.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.feature.home.databinding.ItemHomeBannerBinding
import com.lighthouse.beep.ui.feature.home.model.HomeBannerItem
import com.lighthouse.beep.ui.feature.home.model.loadThumbnail

internal class HomeBannerViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnHomeBannerListener,
    private val binding: ItemHomeBannerBinding = ItemHomeBannerBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    )
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeBannerItem) {
        requestManager.loadThumbnail(item)
            .into(binding.imageBanner)

        binding.imageBanner.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }
}