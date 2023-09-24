package com.lighthouse.beep.ui.feature.home.page.expired

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class ExpiredGifticonViewModel @Inject constructor(): ViewModel() {

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

    private val _selectedBrand = MutableStateFlow<ExpiredBrandItem>(ExpiredBrandItem.All)
    val selectedBrand = _selectedBrand.asStateFlow()

    fun selectBrand(item: ExpiredBrandItem) {
        _selectedBrand.value = item
    }

    val gifticonList = MutableStateFlow(loadGifticonList())

    private fun loadGifticonList(): List<ExpiredGifticonItem> {
        val random = Random(System.currentTimeMillis())
        val oneDay = 24 * 60 * 60 * 1000
        val brandList = brandList.value
        return IntRange(0, 10).map {

            val brand = brandList[random.nextInt(brandList.size - 1) + 1] as ExpiredBrandItem.Item
            val date = random.nextInt(100)
            val created = Date(System.currentTimeMillis() - oneDay * date)
            val expired = Date(System.currentTimeMillis() + oneDay * date)
            ExpiredGifticonItem(it.toLong(), Uri.parse(""), brand.name, "기프티콘 이름", created, expired)
        }.toList()
    }
}