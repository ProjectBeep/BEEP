package com.lighthouse.beep.ui.feature.home.model

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.model.location.DmsPos

internal data class MapGifticonItem(
    val id: Long,
    val thumbnail: GifticonThumbnail,
    val isCashCard: Boolean,
    val remainCash: Int,
    val totalCash: Int,
    val brand: String,
    val name: String,
    val dms: DmsPos,
) {

    // m 단위를 반환
    fun getDistance(other: DmsPos) : Int{
        return dms.distance(other).toInt()
    }
}

internal fun RequestManager.loadThumbnail(item: MapGifticonItem): RequestBuilder<Drawable> {
    return when (val thumbnail = item.thumbnail) {
        is GifticonThumbnail.BuildIn -> {
            load(thumbnail.icon.largeIconRes)
        }
        is GifticonThumbnail.Image -> {
            load(thumbnail.uri)
        }
    }
}


internal class MapGifticonDiff : DiffUtil.ItemCallback<MapGifticonItem>() {
    override fun areItemsTheSame(oldItem: MapGifticonItem, newItem: MapGifticonItem): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem.id == newItem.id -> true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MapGifticonItem, newItem: MapGifticonItem): Boolean {
        return oldItem == newItem
    }
}