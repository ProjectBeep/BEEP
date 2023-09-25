package com.lighthouse.beep.ui.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lighthouse.beep.core.common.exts.calculateNextDayRemainingTime
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.ui.feature.home.adapter.HomeAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredGifticonListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredHeaderListener
import com.lighthouse.beep.ui.feature.home.adapter.map.OnMapGifticonListener
import com.lighthouse.beep.ui.feature.home.databinding.ActivityHomeBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
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
            onMapGifticonListener = onMapGifticonListener,
            onExpiredHeaderListener = onExpiredHeaderListener,
            onExpiredBrandListener = onExpiredBrandListener,
            onExpiredGifticonListener = onExpiredGifticonListener,
        )
    }

    private val onMapGifticonListener = object: OnMapGifticonListener {
        override fun getCurrentDmsPos(): Flow<DmsPos> {
            return flow {
                emit(DmsPos(0.0, 0.0))
            }
        }

        override fun onClick(item: MapGifticonItem) {
        }

        override fun onGotoMapClick() {
        }
    }

    private val onExpiredHeaderListener = object: OnExpiredHeaderListener {
        override fun getSelectedOrder(): Flow<ExpiredOrder> {
            return viewModel.selectedExpiredOrder
        }

        override fun onOrderClick(order: ExpiredOrder) {
            viewModel.setSelectExpiredOrder(order)
        }

        override fun onBrandClick(item: ExpiredBrandItem) {
            viewModel.setSelectBrand(item)
        }

        override fun onGotoEditClick() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpHomeList()
        setUpCollectState()
        setUpClickEvent()
    }

    private fun setUpHomeList() {
        binding.list.adapter = homeAdapter
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