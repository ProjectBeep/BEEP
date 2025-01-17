package com.lighthouse.beep.ui.feature.home.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.lighthouse.beep.core.common.exts.calculateDDay
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal interface HomeItem {

    data class Banner(
        val list: List<HomeBannerItem>
    ) : HomeItem

    data object MapGifticon : HomeItem

    data object GifticonHeader : HomeItem

    data class GifticonItem(
        val id: Long,
        val thumbnail: GifticonThumbnail,
        val isCashCard: Boolean,
        val remainCash: Int,
        val totalCash: Int,
        val brand: String,
        val name: String,
        val expiredDate: Date,
    ) : HomeItem {
        val dday: Int
            get() = expiredDate.calculateDDay()

        val formattedBalance: String
            get() = TextInputFormat.BALANCE.valueToTransformed(remainCash.toString())

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
            oldItem is HomeItem.Banner && newItem is HomeItem.Banner -> oldItem.list == newItem.list
            oldItem is HomeItem.GifticonItem && newItem is HomeItem.GifticonItem -> oldItem.id == newItem.id
            oldItem is HomeItem.GifticonHeader && newItem is HomeItem.GifticonHeader -> true
            oldItem is HomeItem.MapGifticon && newItem is HomeItem.MapGifticon -> true
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return when {
            oldItem is HomeItem.Banner && newItem is HomeItem.Banner -> oldItem.list == newItem.list
            oldItem is HomeItem.GifticonItem && newItem is HomeItem.GifticonItem -> oldItem == newItem
            oldItem is HomeItem.GifticonHeader && newItem is HomeItem.GifticonHeader -> true
            oldItem is HomeItem.MapGifticon && newItem is HomeItem.MapGifticon -> true
            else -> false
        }
    }
}