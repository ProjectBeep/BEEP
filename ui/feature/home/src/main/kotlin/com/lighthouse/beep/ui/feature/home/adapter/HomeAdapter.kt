package com.lighthouse.beep.ui.feature.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredGifticonViewHolder
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredHeaderViewHolder
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredTitleViewHolder
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredGifticonListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredHeaderListener
import com.lighthouse.beep.ui.feature.home.adapter.map.MapGifticonSection
import com.lighthouse.beep.ui.feature.home.adapter.map.OnMapGifticonListener
import com.lighthouse.beep.ui.feature.home.adapter.map.OnMapGifticonSectionListener
import com.lighthouse.beep.ui.feature.home.model.HomeDiff
import com.lighthouse.beep.ui.feature.home.model.HomeItem

internal class HomeAdapter(
    private val onMapGifticonSectionListener: OnMapGifticonSectionListener,
    private val onMapGifticonListener: OnMapGifticonListener,
    private val onExpiredHeaderListener: OnExpiredHeaderListener,
    private val onExpiredBrandListener: OnExpiredBrandListener,
    private val onExpiredGifticonListener: OnExpiredGifticonListener,
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(HomeDiff()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is HomeItem.MapGifticon -> TYPE_MAP_SECTION
            is HomeItem.ExpiredTitle -> TYPE_EXPIRED_TITLE
            is HomeItem.ExpiredHeader -> TYPE_EXPIRED_HEADER
            is HomeItem.ExpiredGifticonItem -> TYPE_EXPIRED_GIFTICON_ITEM
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MAP_SECTION -> MapGifticonSection(parent, onMapGifticonSectionListener, onMapGifticonListener)
            TYPE_EXPIRED_TITLE -> ExpiredTitleViewHolder(parent)
            TYPE_EXPIRED_HEADER -> ExpiredHeaderViewHolder(parent, onExpiredHeaderListener, onExpiredBrandListener)
            TYPE_EXPIRED_GIFTICON_ITEM -> ExpiredGifticonViewHolder(parent, onExpiredGifticonListener)
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is MapGifticonSection && item is HomeItem.MapGifticon -> holder.bind(item)
            holder is ExpiredHeaderViewHolder && item is HomeItem.ExpiredHeader -> holder.bind(item)
            holder is ExpiredGifticonViewHolder && item is HomeItem.ExpiredGifticonItem -> holder.bind(item)
        }
    }

    companion object {
        private const val TYPE_MAP_SECTION = 1
        private const val TYPE_EXPIRED_TITLE = 2
        private const val TYPE_EXPIRED_HEADER = 3
        private const val TYPE_EXPIRED_GIFTICON_ITEM = 4
    }
}