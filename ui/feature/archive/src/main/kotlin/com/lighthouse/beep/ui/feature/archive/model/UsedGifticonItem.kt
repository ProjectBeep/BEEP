package com.lighthouse.beep.ui.feature.archive.model

import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal data class UsedGifticonItem(
    val id: Long,
    val thumbnail: GifticonThumbnail,
    val brand: String,
    val name: String,
    val expireAt: Date,
) {

    val formattedExpiredDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return formatter.format(expireAt)
        }
}

internal val Diff = object: DiffUtil.ItemCallback<UsedGifticonItem>() {
    override fun areItemsTheSame(oldItem: UsedGifticonItem, newItem: UsedGifticonItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UsedGifticonItem, newItem: UsedGifticonItem): Boolean {
        return oldItem == newItem
    }
}

internal fun List<GifticonListItem>.toUsedModel(): List<UsedGifticonItem> {
    return map {
        it.toUsedModel()
    }
}

internal fun GifticonListItem.toUsedModel(): UsedGifticonItem {
    return UsedGifticonItem(
        id = id,
        thumbnail = thumbnail,
        brand = displayBrand,
        name = name,
        expireAt = expireAt
    )
}