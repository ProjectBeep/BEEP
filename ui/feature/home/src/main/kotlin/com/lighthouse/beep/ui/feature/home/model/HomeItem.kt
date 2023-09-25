package com.lighthouse.beep.ui.feature.home.model

import android.annotation.SuppressLint
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.lighthouse.beep.core.common.exts.calculateDDay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal interface HomeItem {

    data class MapGifticon(val list: List<MapGifticonItem>): HomeItem

    data object ExpiredTitle : HomeItem

    data class ExpiredHeader(val list: List<ExpiredBrandItem>) : HomeItem

    data class ExpiredGifticonItem(
        val id: Long,
        val thumbnailUri: Uri,
        val brand: String,
        val name: String,
        val expiredDate: Date,
        val createdDate: Date,
    ): HomeItem {
        val dday: Int
            get() = expiredDate.calculateDDay()

        val formattedExpiredDate: String
            get() {
                val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                return formatter.format(expiredDate)
            }
    }
}

internal class HomeDiff : ItemCallback<HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem is HomeItem.MapGifticon && newItem is HomeItem.MapGifticon -> true
            oldItem is HomeItem.ExpiredHeader && newItem is HomeItem.ExpiredHeader -> true
            oldItem is HomeItem.ExpiredGifticonItem && newItem is HomeItem.ExpiredGifticonItem && oldItem.id == newItem.id -> true
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return when {
            oldItem is HomeItem.MapGifticon && newItem is HomeItem.MapGifticon -> oldItem.list == newItem.list
            oldItem is HomeItem.ExpiredHeader && newItem is HomeItem.ExpiredHeader -> oldItem.list == newItem.list
            oldItem is HomeItem.ExpiredGifticonItem && newItem is HomeItem.ExpiredGifticonItem -> oldItem == newItem
            else -> false
        }
    }
}