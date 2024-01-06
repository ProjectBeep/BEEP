package com.lighthouse.beep.ui.feature.home.page.home.section.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lighthouse.beep.core.ui.exts.getScrollInfo
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredHeaderBinding
import com.lighthouse.beep.ui.feature.home.model.BrandItem
import com.lighthouse.beep.ui.feature.home.model.GifticonOrder
import com.lighthouse.beep.ui.feature.home.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.theme.R as ThemeR

internal class GifticonHeaderViewHolder(
    parent: ViewGroup,
    private val listener: OnGifticonHeaderSectionListener,
    private val binding: ItemExpiredHeaderBinding = ItemExpiredHeaderBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<HomeItem.GifticonHeader>(binding.root) {

    private val brandAdapter = GifticonBrandChipAdapter(listener, ::onBrandItemClick)
    private val brandLayoutManager = CenterScrollLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private val brandScrollListener = object: RecyclerView.OnScrollListener() {
        var isScrollSyncActivated = false
            private set

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                onBrandScroll(recyclerView)
                isScrollSyncActivated = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isScrollSyncActivated) {
                onBrandScroll(recyclerView)
            }
        }

        fun syncScrollActive() {
            isScrollSyncActivated = true
        }

        private fun onBrandScroll(recyclerView: RecyclerView) {
            val scrollInfo = recyclerView.getScrollInfo()
            listener.onBrandScroll(scrollInfo)
        }
    }

    init {
        binding.listBrand.adapter = brandAdapter
        binding.listBrand.layoutManager = brandLayoutManager
        binding.listBrand.addOnScrollListener(brandScrollListener)

        binding.tabExpired.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab ?: return
                listener.onOrderClick(GifticonOrder.entries[tab.position])
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        GifticonOrder.entries.forEach {
            val tab = binding.tabExpired.newTab().apply {
                setText(it.titleRes)
            }
            binding.tabExpired.addTab(tab)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.GifticonHeader) {
        listener.getSelectedOrder().collect(lifecycleOwner) { order ->
            if (binding.tabExpired.selectedTabPosition == order.ordinal){
                return@collect
            }
            val tab = binding.tabExpired.getTabAt(order.ordinal)
            binding.tabExpired.selectTab(tab)
        }

        listener.getViewModeFlow().collect(lifecycleOwner) { mode ->
            when(mode) {
                GifticonViewMode.VIEW -> {
                    binding.textEdit.setText(R.string.edit_gifticon)
                    binding.iconEdit.setImageResource(ThemeR.drawable.icon_edit)
                }
                GifticonViewMode.EDIT -> {
                    binding.textEdit.setText(R.string.edit_cancel)
                    binding.iconEdit.setImageResource(ThemeR.drawable.icon_edit_cancel)
                }
            }
        }

        listener.getBrandListFlow().collect(lifecycleOwner) { list ->
            brandAdapter.submitList(list)
        }

        listener.getBrandScrollInfo().collect(lifecycleOwner) { info ->
            if (brandScrollListener.isScrollSyncActivated){
                return@collect
            }
            brandLayoutManager.scrollToPositionWithOffset(info.position, info.offset)
        }
    }

    override fun onSetUpClickEvent(item: HomeItem.GifticonHeader) {
        binding.btnEdit.setOnThrottleClickListener {
            listener.onGotoEditClick()
        }
    }

    private fun onBrandItemClick(item: BrandItem, position: Int) {
        brandScrollListener.syncScrollActive()
        listener.onBrandClick(item)
        binding.listBrand.smoothScrollToPosition(position)
    }
}