package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredHeaderBinding
import com.lighthouse.beep.ui.feature.home.databinding.TabExpiredBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.BrandScrollInfo

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
        var isSyncScroll = false
            private set

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                onBrandScroll(recyclerView)
                isSyncScroll = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isSyncScroll) {
                onBrandScroll(recyclerView)
            }
        }

        fun syncScroll() {
            isSyncScroll = true
        }

        private fun onBrandScroll(recyclerView: RecyclerView) {
            val position = brandLayoutManager.findFirstVisibleItemPosition()
            val viewOffset = brandLayoutManager.findViewByPosition(position)?.left ?: 0
            val viewSpace = if(position > 0) 4.dp else 0
            val offset = viewOffset - viewSpace - recyclerView.paddingLeft
            listener.onBrandScroll(BrandScrollInfo(position, offset))
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
                val tabBinding = tab?.tag as? TabExpiredBinding ?: return
                tabBinding.textTitle.setTextAppearance(ThemeR.style.Text_Body1)
                tabBinding.textTitle.setTextColor(context.getColor(ThemeR.color.black))

                listener.onOrderClick(ExpiredOrder.entries[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabBinding = tab?.tag as? TabExpiredBinding ?: return
                tabBinding.textTitle.setTextAppearance(ThemeR.style.Text_Body2)
                tabBinding.textTitle.setTextColor(context.getColor(ThemeR.color.font_medium_gray))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        ExpiredOrder.entries.forEach {
            val tab = binding.tabExpired.newTab().apply {
                val tabBinding = TabExpiredBinding.inflate(LayoutInflater.from(context))
                tabBinding.textTitle.setText(it.titleRes)
                tag = tabBinding
                customView = tabBinding.root
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
            if (brandScrollListener.isSyncScroll){
                return@collect
            }
            brandLayoutManager.scrollToPositionWithOffset(info.position, info.offset)
        }
    }

    private fun onBrandItemClick(item: ExpiredBrandItem, position: Int) {
        brandScrollListener.syncScroll()
        listener.onBrandClick(item)
        binding.listBrand.smoothScrollToPosition(position)
    }
}