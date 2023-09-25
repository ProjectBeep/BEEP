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

internal class ExpiredHeaderViewHolder(
    parent: ViewGroup,
    private val listener: OnExpiredHeaderListener,
    private val onExpiredBrandListener: OnExpiredBrandListener,
    private val binding: ItemExpiredHeaderBinding = ItemExpiredHeaderBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<HomeItem.ExpiredHeader>(binding.root) {

    private val brandAdapter = ExpiredBrandChipAdapter(onExpiredBrandListener, ::onBrandItemClick)

    init {
        binding.listBrand.adapter = brandAdapter
        binding.listBrand.layoutManager = CenterScrollLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.listBrand.addItemDecoration(LinearItemDecoration(8.dp))

        binding.btnEdit.setOnThrottleClickListener {
            listener.onGotoEditClick()
        }

        ExpiredOrder.entries.forEach {
            val tab = binding.tabExpired.newTab().apply {
                val tabBinding = TabExpiredBinding.inflate(LayoutInflater.from(context))
                tabBinding.textTitle.setText(it.titleRes)
                tag = tabBinding
                customView = tabBinding.root
            }
            binding.tabExpired.addTab(tab)
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
    }

    override fun bind(item: HomeItem.ExpiredHeader) {
        super.bind(item)

        brandAdapter.submitList(item.list)
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.ExpiredHeader) {
        listener.getSelectedOrder().collect(lifecycleOwner) { order ->
            if (binding.tabExpired.selectedTabPosition == order.ordinal){
                return@collect
            }
            val tab = binding.tabExpired.getTabAt(order.ordinal)
            binding.tabExpired.selectTab(tab)
        }
    }

    private fun onBrandItemClick(item: ExpiredBrandItem, position: Int) {
        listener.onBrandClick(item)
        binding.listBrand.smoothScrollToPosition(position)
    }
}