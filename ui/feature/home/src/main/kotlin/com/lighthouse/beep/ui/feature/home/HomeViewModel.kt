package com.lighthouse.beep.ui.feature.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import com.lighthouse.beep.ui.feature.home.model.BrandScrollInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.random.Random

internal class HomeViewModel : ViewModel() {

    private val _selectedExpiredOrder = MutableStateFlow(ExpiredOrder.D_DAY)
    val selectedExpiredOrder = _selectedExpiredOrder.asStateFlow()

    fun setSelectExpiredOrder(order: ExpiredOrder) {
        _selectedExpiredOrder.value = order
    }

    private val _selectedBrand = MutableStateFlow<ExpiredBrandItem>(ExpiredBrandItem.All)
    val selectedBrand = _selectedBrand.asStateFlow()

    fun setSelectBrand(item: ExpiredBrandItem) {
        _selectedBrand.value = item
    }

    private val _brandScrollInfo = MutableStateFlow(BrandScrollInfo.None)
    val brandScrollInfo = _brandScrollInfo.asStateFlow()

    fun setBrandScrollInfo(info: BrandScrollInfo) {
        _brandScrollInfo.value = info
    }

    val brandList = MutableStateFlow(
        listOf(
            ExpiredBrandItem.All,
            ExpiredBrandItem.Item("스타벅스"),
            ExpiredBrandItem.Item("CU"),
            ExpiredBrandItem.Item("GS25"),
            ExpiredBrandItem.Item("파리바게트"),
            ExpiredBrandItem.Item("버거킹"),
            ExpiredBrandItem.Item("투썸플레이스"),
            ExpiredBrandItem.Item("롯데리아"),
        )
    )

    val mapGifticonList = MutableStateFlow(loadMapGifticonList())

    private fun loadMapGifticonList(): List<MapGifticonItem> {
        val random = Random(System.currentTimeMillis())
        val brandList = brandList.value
        return IntRange(0, 10).map {
            val brand = brandList[random.nextInt(brandList.size - 1) + 1] as ExpiredBrandItem.Item
            val lat = random.nextDouble(0.01)
            val lon = random.nextDouble(0.01)
            MapGifticonItem(it.toLong(), Uri.parse(""), brand.name, "기프티콘 이름", DmsPos(lat, lon))
        }.toList()
    }

    private fun loadExpiredGifticonList(): List<HomeItem.ExpiredGifticonItem> {
        val random = Random(System.currentTimeMillis())
        val oneDay = 24 * 60 * 60 * 1000L
        val brandList = brandList.value
        return IntRange(0, 10).map {
            val brand = brandList[random.nextInt(brandList.size - 1) + 1] as ExpiredBrandItem.Item
            val date = random.nextInt(100)
            val expired = Date(System.currentTimeMillis() + oneDay * date)
            val created = Date(System.currentTimeMillis() - oneDay * date)
            HomeItem.ExpiredGifticonItem(it.toLong(), Uri.parse(""), brand.name, "기프티콘 이름", expired, created)
        }.toList()
    }

    val homeList = MutableStateFlow(
        mutableListOf<HomeItem>().apply {
            add(HomeItem.MapGifticon)
            add(HomeItem.ExpiredTitle)
            add(HomeItem.ExpiredHeader)
            addAll(loadExpiredGifticonList())
        }
    )

    val expiredHeaderIndex = homeList.value.indexOfFirst {
        it is HomeItem.ExpiredHeader
    }

    val expiredGifticonFirstIndex = homeList.value.indexOfFirst {
        it is HomeItem.ExpiredGifticonItem
    }
}