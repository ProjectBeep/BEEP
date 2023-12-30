package com.lighthouse.beep.ui.feature.home.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.common.exts.calculateNextDayRemainingTime
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.model.BrandItem
import com.lighthouse.beep.ui.feature.home.model.BrandItemDiff
import com.lighthouse.beep.ui.feature.home.model.GifticonOrder
import com.lighthouse.beep.ui.feature.home.model.GifticonQuery
import com.lighthouse.beep.ui.feature.home.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.home.model.HomeBannerItem
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class HomeMainViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository,
) : ViewModel() {

    var requestStopAnimation = false
        private set

    fun completeStopAnimation() {
        requestStopAnimation = false
    }

    private val _selectedOrder = MutableStateFlow(GifticonOrder.D_DAY)
    val selectedOrder = _selectedOrder.asStateFlow()

    fun selectGifticonOrder(order: GifticonOrder) {
        if (order == selectedOrder.value) {
            return
        }
        requestStopAnimation = true
        _selectedOrder.value = order
    }

    private val _selectedBrandItem = MutableStateFlow<BrandItem>(BrandItem.All)
    val selectedBrandItem = _selectedBrandItem.asStateFlow()

    val brandList = BeepAuth.currentUid.flatMapLatest { userUid ->
        gifticonRepository.getBrandCategoryList(userUid).map { brandList ->
            listOf(BrandItem.All) + brandList.map { BrandItem.Item(it.displayBrand) }
        }
    }

    fun selectBrand(item: BrandItem) {
        if (BrandItemDiff.areItemsTheSame(item, selectedBrandItem.value)) {
            return
        }

        _selectedBrandItem.value = item
    }

    private val _gifticonViewMode = MutableStateFlow(GifticonViewMode.VIEW)
    val gifticonViewMode = _gifticonViewMode.asStateFlow()

    private val selectedGifticonList = mutableListOf<HomeItem.GifticonItem>()

    private val _selectedGifticonListFlow =
        MutableSharedFlow<List<HomeItem.GifticonItem>>(replay = 1)
    val selectedGifticonListFlow = _selectedGifticonListFlow.asSharedFlow()

    fun selectGifticon(item: HomeItem.GifticonItem) {
        if (selectedGifticonList.find { it.id == item.id } != null) {
            selectedGifticonList.remove(item)
        } else {
            selectedGifticonList.add(item)
        }
        viewModelScope.launch {
            _selectedGifticonListFlow.emit(selectedGifticonList)
        }
    }



    fun deleteSelectedGifticon() {
        viewModelScope.launch {
            val gifticonIdList = selectedGifticonList.map { it.id }
            gifticonRepository.deleteGifticon(BeepAuth.userUid, gifticonIdList)
            setGifticonViewModel(GifticonViewMode.VIEW)
        }
    }

    fun useSelectedGifticon() {
        viewModelScope.launch {
            val gifticonIdList = selectedGifticonList.map { it.id }
            gifticonRepository.useGifticonList(BeepAuth.userUid, gifticonIdList)
            setGifticonViewModel(GifticonViewMode.VIEW)
        }
    }

    private fun initSelectedGifticonList() {
        selectedGifticonList.clear()
        viewModelScope.launch {
            _selectedGifticonListFlow.emit(emptyList())
        }
    }

    fun toggleGifticonViewModel() {
        val nextViewMode = when (gifticonViewMode.value) {
            GifticonViewMode.VIEW -> GifticonViewMode.EDIT
            GifticonViewMode.EDIT -> GifticonViewMode.VIEW
        }
        setGifticonViewModel(nextViewMode)
    }

    private fun setGifticonViewModel(mode: GifticonViewMode) {
        if (mode == GifticonViewMode.VIEW) {
            initSelectedGifticonList()
        }
        _gifticonViewMode.value = mode
    }

    private val gifticonList = combine(
        BeepAuth.currentUid,
        selectedOrder,
        selectedBrandItem,
    ){ userUid, order, brand ->
        GifticonQuery(userUid, order, brand)
    }.flatMapLatest { (userUid, order, brand) ->
        when (brand) {
            is BrandItem.All -> gifticonRepository.getGifticonList(
                userId = userUid,
                isUsed = false,
                gifticonSortBy = order.sortBy,
                isAsc = order == GifticonOrder.D_DAY,
            )
            is BrandItem.Item -> gifticonRepository.getGifticonListByBrand(
                userId = userUid,
                brand = brand.name,
                gifticonSortBy = order.sortBy,
                isAsc = order == GifticonOrder.D_DAY,
            )
        }
    }

    private val _brandScrollInfo = MutableStateFlow(ScrollInfo.None)
    val brandScrollInfo = _brandScrollInfo.asStateFlow()

    fun setBrandScrollInfo(info: ScrollInfo) {
        _brandScrollInfo.value = info
    }

    val nextDayRemainingTimeFlow = flow {
        while (true) {
            delay(Date().calculateNextDayRemainingTime())
            emit(Unit)
        }
    }

    private val homeBannerList = listOf(
        HomeBannerItem.BuiltIn(R.drawable.img_banner_location_coming_soon),
    )

    private val _isShowStickyHeader = MutableStateFlow(false)
    val isShowStickyHeader = _isShowStickyHeader.asStateFlow()

    private var expiredHeaderIndex = -1

    fun setCurrentPosition(position: Int) {
        _isShowStickyHeader.value = expiredHeaderIndex <= position
    }

    val homeList = gifticonList.map { gifticonList ->
        listOf(
            HomeItem.Banner(homeBannerList),
            HomeItem.GifticonHeader
        ) + gifticonList.map {
            HomeItem.GifticonItem(
                id = it.id,
                thumbnail = it.thumbnail,
                isCashCard = it.isCashCard,
                remainCash = it.remainCash,
                totalCash = it.totalCash,
                brand = it.displayBrand,
                name = it.name,
                expiredDate = it.expireAt,
            )
        }
    }.onEach { homeList ->
        expiredHeaderIndex = homeList.indexOfFirst {
            it is HomeItem.GifticonHeader
        }
    }

    init {
        initSelectedGifticonList()
    }
}