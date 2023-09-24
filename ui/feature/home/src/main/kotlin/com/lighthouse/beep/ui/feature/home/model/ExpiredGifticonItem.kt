package com.lighthouse.beep.ui.feature.home.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.core.common.exts.calculateDDay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal data class ExpiredGifticonItem(
    val id: Long,
    val thumbnailUri: Uri,
    val brand: String,
    val name: String,
    val expiredDate: Date,
    val createdDate: Date,
) {
    val dday: Int
        get() = expiredDate.calculateDDay()

    val formattedExpiredDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return formatter.format(expiredDate)
        }
}

internal class ExpiredGifticonDiff : DiffUtil.ItemCallback<ExpiredGifticonItem>() {
    override fun areItemsTheSame(
        oldItem: ExpiredGifticonItem,
        newItem: ExpiredGifticonItem
    ): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem.id == newItem.id -> true
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: ExpiredGifticonItem,
        newItem: ExpiredGifticonItem
    ): Boolean {
        return oldItem == newItem
    }
}