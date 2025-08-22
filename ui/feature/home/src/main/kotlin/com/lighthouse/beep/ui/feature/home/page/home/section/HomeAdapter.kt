package com.lighthouse.beep.ui.feature.home.page.home.section

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.home.page.home.section.gifticon.GifticonViewHolder
import com.lighthouse.beep.ui.feature.home.page.home.section.header.GifticonHeaderViewHolder
import com.lighthouse.beep.ui.feature.home.page.home.section.gifticon.OnGifticonListener
import com.lighthouse.beep.ui.feature.home.page.home.section.header.OnGifticonHeaderSectionListener
import com.lighthouse.beep.ui.feature.home.page.home.section.map.MapGifticonSection
import com.lighthouse.beep.ui.feature.home.page.home.section.map.OnMapGifticonSectionListener
import com.lighthouse.beep.ui.feature.home.model.HomeDiff
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.page.home.section.banner.HomeBannerSection
import com.lighthouse.beep.ui.feature.home.page.home.section.banner.OnHomeBannerSectionListener

internal class HomeAdapter(
    private val requestManager: RequestManager,
    private val onHomeBannerSectionListener: OnHomeBannerSectionListener,
    private val onMapGifticonSectionListener: OnMapGifticonSectionListener,
    private val onGifticonHeaderSectionListener: OnGifticonHeaderSectionListener,
    private val onGifticonListener: OnGifticonListener,
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(HomeDiff()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is HomeItem.Banner -> TYPE_HOME_BANNER
            is HomeItem.MapGifticon -> TYPE_MAP_SECTION
            is HomeItem.GifticonHeader -> TYPE_EXPIRED_HEADER
            is HomeItem.GifticonItem -> TYPE_EXPIRED_GIFTICON_ITEM
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HOME_BANNER -> HomeBannerSection(parent, requestManager, onHomeBannerSectionListener)
            TYPE_MAP_SECTION -> MapGifticonSection(parent, requestManager, onMapGifticonSectionListener)
            TYPE_EXPIRED_HEADER -> GifticonHeaderViewHolder(parent, onGifticonHeaderSectionListener)
            TYPE_EXPIRED_GIFTICON_ITEM -> GifticonViewHolder(parent, requestManager, onGifticonListener)
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is HomeBannerSection && item is HomeItem.Banner -> holder.bind(item)
            holder is MapGifticonSection && item is HomeItem.MapGifticon -> holder.bind(item)
            holder is GifticonHeaderViewHolder && item is HomeItem.GifticonHeader -> holder.bind(item)
            holder is GifticonViewHolder && item is HomeItem.GifticonItem -> holder.bind(item)
        }
    }

    companion object {
        private const val TYPE_HOME_BANNER = 1
        private const val TYPE_MAP_SECTION = 2
        private const val TYPE_EXPIRED_HEADER = 3
        private const val TYPE_EXPIRED_GIFTICON_ITEM = 4
    }
}