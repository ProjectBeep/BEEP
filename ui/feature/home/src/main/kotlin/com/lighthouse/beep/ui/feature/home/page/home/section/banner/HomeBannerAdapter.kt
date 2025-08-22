package com.lighthouse.beep.ui.feature.home.page.home.section.banner

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.home.model.HomeBannerDiff
import com.lighthouse.beep.ui.feature.home.model.HomeBannerItem

internal class HomeBannerAdapter(
    private val requestManager: RequestManager,
    private val listener: OnHomeBannerListener,
) : ListAdapter<HomeBannerItem, HomeBannerViewHolder>(HomeBannerDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(parent, requestManager, listener)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}