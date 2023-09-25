package com.lighthouse.beep.ui.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lighthouse.beep.core.common.exts.calculateNextDayRemainingTime
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.ui.feature.home.adapter.HomeAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredHeaderViewHolder
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredGifticonListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredHeaderListener
import com.lighthouse.beep.ui.feature.home.adapter.map.OnMapGifticonListener
import com.lighthouse.beep.ui.feature.home.adapter.map.OnMapGifticonSectionListener
import com.lighthouse.beep.ui.feature.home.databinding.ActivityHomeBinding
import com.lighthouse.beep.ui.feature.home.decorator.HomeItemDecoration
import com.lighthouse.beep.ui.feature.home.decorator.HomeItemDecorationCallback
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import com.lighthouse.beep.ui.feature.home.model.BrandScrollInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

@AndroidEntryPoint
internal class HomeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    private val homeAdapter by lazy {
        HomeAdapter(
            onMapGifticonSectionListener = onMapGifticonSectionListener,
            onMapGifticonListener = onMapGifticonListener,
            onExpiredHeaderListener = onExpiredHeaderListener,
            onExpiredBrandListener = onExpiredBrandListener,
            onExpiredGifticonListener = onExpiredGifticonListener,
        )
    }

    private val onMapGifticonSectionListener = object: OnMapGifticonSectionListener {
        override fun getMapGifticonListFlow(): Flow<List<MapGifticonItem>> {
            return viewModel.mapGifticonList
        }

        override fun onGotoMapClick() {
        }
    }

    private val onMapGifticonListener = object: OnMapGifticonListener {
        override fun getCurrentDmsPosFlow(): Flow<DmsPos> {
            return flow {
                emit(DmsPos(0.0, 0.0))
            }
        }

        override fun onClick(item: MapGifticonItem) {
        }
    }

    private val onExpiredHeaderListener = object: OnExpiredHeaderListener {
        override fun getBrandListFlow(): Flow<List<ExpiredBrandItem>> {
            return viewModel.brandList
        }

        override fun getSelectedOrder(): Flow<ExpiredOrder> {
            return viewModel.selectedExpiredOrder
        }

        override fun getBrandScrollInfo(): Flow<BrandScrollInfo> {
            return viewModel.brandScrollInfo
        }

        override fun onOrderClick(order: ExpiredOrder) {
            viewModel.setSelectExpiredOrder(order)
        }

        override fun onBrandClick(item: ExpiredBrandItem) {
            viewModel.setSelectBrand(item)
        }

        override fun onGotoEditClick() {
        }

        override fun onBrandScroll(scrollInfo: BrandScrollInfo) {
            viewModel.setBrandScrollInfo(scrollInfo)
        }
    }

    private val onExpiredBrandListener = object: OnExpiredBrandListener {
        override fun getSelectedFlow(): Flow<ExpiredBrandItem> {
            return viewModel.selectedBrand
        }
    }

    private val onExpiredGifticonListener = object: OnExpiredGifticonListener{
        override fun getNextDayEventFlow(): Flow<Unit> {
            return flow {
                while (true) {
                    delay(Date().calculateNextDayRemainingTime())
                    emit(Unit)
                }
            }
        }

        override fun onClick(item: HomeItem.ExpiredGifticonItem) {

        }
    }

    private val homeItemDecorationCallback = object: HomeItemDecorationCallback {
        override fun isShowExpiredHeader(position: Int): Boolean {
            val isShow = viewModel.expiredHeaderIndex <= position
            binding.containerStickyHeader.isVisible = isShow
            return isShow
        }

        override fun getHeaderViewHolder(): ExpiredHeaderViewHolder {
            return ExpiredHeaderViewHolder(binding.list, onExpiredHeaderListener, onExpiredBrandListener)
        }

        override fun isExpiredGifticonFirst(position: Int): Boolean {
            return viewModel.expiredGifticonFirstIndex == position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpHomeList()
        setUpCollectState()
        setUpClickEvent()

        val header = homeItemDecorationCallback.getHeaderViewHolder()
        header.bind(HomeItem.ExpiredHeader)
        binding.containerStickyHeader.addView(
            header.itemView
        )
        binding.containerStickyHeader.preventTouchPropagation()
    }

    private fun setUpHomeList() {
        binding.list.adapter = homeAdapter
        binding.list.addItemDecoration(HomeItemDecoration(homeItemDecorationCallback))
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.homeList.collect {
                homeAdapter.submitList(it)
            }
        }
    }

    private fun setUpClickEvent() {

    }
}