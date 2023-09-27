package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.getScrollInfo
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredHeaderBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem

internal class ExpiredHeaderViewHolder(
    parent: ViewGroup,
    private val listener: OnExpiredHeaderListener,
    private val onExpiredBrandListener: OnExpiredBrandListener,
    private val binding: ItemExpiredHeaderBinding = ItemExpiredHeaderBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<HomeItem.ExpiredHeader>(binding.root) {

    private val brandAdapter = ExpiredBrandChipAdapter(onExpiredBrandListener, ::onBrandItemClick)
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
            val scrollInfo = recyclerView.getScrollInfo { position ->
                if(position > 0) 4.dp else 0
            }
            listener.onBrandScroll(scrollInfo)
        }
    }

    init {
        binding.listBrand.adapter = brandAdapter
        binding.listBrand.layoutManager = brandLayoutManager
        binding.listBrand.addItemDecoration(LinearItemDecoration(8.dp))
        binding.listBrand.addOnScrollListener(brandScrollListener)

        binding.btnEdit.setOnThrottleClickListener {
            listener.onGotoEditClick()
        }

        binding.tabExpired.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab ?: return
                listener.onOrderClick(ExpiredOrder.entries[tab.position])
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        ExpiredOrder.entries.forEach {
            val tab = binding.tabExpired.newTab().apply {
                setText(it.titleRes)
            }
            binding.tabExpired.addTab(tab)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.ExpiredHeader) {
        listener.getSelectedOrder().collect(lifecycleOwner) { order ->
            if (binding.tabExpired.selectedTabPosition == order.ordinal){
                return@collect
            }
            val tab = binding.tabExpired.getTabAt(order.ordinal)
            binding.tabExpired.selectTab(tab)
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

    private fun onBrandItemClick(item: ExpiredBrandItem, position: Int) {
        brandScrollListener.syncScrollActive()
        listener.onBrandClick(item)
        binding.listBrand.smoothScrollToPosition(position)
    }
}