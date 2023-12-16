package com.lighthouse.beep.ui.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lighthouse.beep.core.ui.exts.checkSelfPermissions
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.permission.dialog.StoragePermissionDialog
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@AndroidEntryPoint
internal class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var navigator: AppNavigator

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedList = result.filter { !it.value }.map { it.key }
            if (deniedList.isEmpty()) {
                gotoGallery()
            } else {
                showRequestPermissionDialog()
            }
        }

    private val homeAdapter by lazy {
        HomeAdapter(
            onMapGifticonSectionListener = onMapGifticonSectionListener,
            onMapGifticonListener = onMapGifticonListener,
            onExpiredHeaderListener = onExpiredHeaderListener,
            onExpiredBrandListener = onExpiredBrandListener,
            onExpiredGifticonListener = onExpiredGifticonListener,
        )
    }

    private val onMapGifticonSectionListener = object : OnMapGifticonSectionListener {
        override fun getMapGifticonListFlow(): Flow<List<MapGifticonItem>> {
            return viewModel.mapGifticonList
        }

        override fun onGotoMapClick() {
        }
    }

    private val onMapGifticonListener = object : OnMapGifticonListener {
        override fun getCurrentDmsPosFlow(): Flow<DmsPos> {
            return flow {
                emit(DmsPos(0.0, 0.0))
            }
        }

        override fun onClick(item: MapGifticonItem) {
        }
    }

    private val onExpiredHeaderListener = object : OnExpiredHeaderListener {
        override fun getBrandListFlow(): Flow<List<ExpiredBrandItem>> {
            return viewModel.brandList
        }

        override fun getSelectedOrder(): Flow<ExpiredOrder> {
            return viewModel.selectedExpiredOrder
        }

        override fun getBrandScrollInfo(): Flow<ScrollInfo> {
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

        override fun onBrandScroll(scrollInfo: ScrollInfo) {
            viewModel.setBrandScrollInfo(scrollInfo)
        }
    }

    private val onExpiredBrandListener = object : OnExpiredBrandListener {
        override fun getSelectedFlow(): Flow<ExpiredBrandItem> {
            return viewModel.selectedBrand
        }
    }

    private val onExpiredGifticonListener = object : OnExpiredGifticonListener {
        override fun getNextDayEventFlow(): Flow<Unit> {
            return viewModel.nextDayRemainingTimeFlow
        }

        override fun onClick(item: HomeItem.ExpiredGifticonItem) {

        }
    }

    private val homeItemDecorationCallback = object : HomeItemDecorationCallback {
        override fun onTopItemPosition(position: Int) {
            val isShow = viewModel.expiredHeaderIndex <= position
            binding.containerStickyHeader.isVisible = isShow
        }

        override fun getExpiredGifticonFirstIndex(): Int {
            return viewModel.expiredGifticonFirstIndex
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpExpiredStickyHeader()
        setUpHomeList()
        setUpCollectState()
        setUpClickEvent()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("TEST", "result : ${checkSelfPermissions(BeepPermission.storage)}")
//    }

    private fun setUpExpiredStickyHeader() {
        val header =
            ExpiredHeaderViewHolder(binding.list, onExpiredHeaderListener, onExpiredBrandListener)
        header.bind(HomeItem.ExpiredHeader)
        binding.containerStickyHeader.addView(header.itemView)
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
        binding.btnGotoSetting.setOnClickListener(createThrottleClickListener {

        })

        binding.btnGotoGifticonAdd.setOnClickListener(createThrottleClickListener {
            if (checkSelfPermissions(BeepPermission.storage)) {
                gotoGallery()
            } else {
                requestPermissions(BeepPermission.storage)
            }
        })
    }

    private fun showRequestPermissionDialog() {
        show(StoragePermissionDialog.TAG) {
            StoragePermissionDialog().apply {
                setOnPermissionListener { grant ->
                    if (grant) {
                        gotoGallery()
                    }
                }
            }
        }
    }

    private fun gotoGallery() {
        val intent = navigator.getIntent(this, ActivityNavItem.Gallery)
        startActivity(intent)
    }

    private fun requestPermissions(permissions: Array<String>) {
        galleryLauncher.launch(permissions)
    }
}