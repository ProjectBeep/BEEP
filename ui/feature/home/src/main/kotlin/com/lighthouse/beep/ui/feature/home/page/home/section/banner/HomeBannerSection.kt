package com.lighthouse.beep.ui.feature.home.page.home.section.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.home.databinding.SectionHomeBannerBinding
import com.lighthouse.beep.ui.feature.home.model.HomeItem

internal class HomeBannerSection(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnHomeBannerSectionListener,
    private val binding: SectionHomeBannerBinding = SectionHomeBannerBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
): RecyclerView.ViewHolder(binding.root) {

    private val bannerAdapter = HomeBannerAdapter(requestManager, listener)

    init {
        binding.listBanner.adapter = bannerAdapter
        PagerSnapHelper().attachToRecyclerView(binding.listBanner)
        binding.indicatorBanner.attachToRecyclerView(binding.listBanner)
    }

    fun bind(item: HomeItem.Banner) {
        bannerAdapter.submitList(item.list)

        binding.indicatorBanner.isVisible = item.list.size > 1
    }
}