package com.lighthouse.beep.ui.feature.home.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.location.DmsPos

internal data class MapGifticonItem(
    val id: Long,
    val thumbnailUri: Uri,
    val brand: String,
    val name: String,
    val dms: DmsPos,
) {

    // m 단위를 반환
    fun getDistance(other: DmsPos) : Int{
        return dms.distance(other).toInt()
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