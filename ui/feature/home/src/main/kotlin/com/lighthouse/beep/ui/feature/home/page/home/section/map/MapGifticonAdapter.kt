package com.lighthouse.beep.ui.feature.home.page.home.section.map

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.home.model.MapGifticonDiff
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem

internal class MapGifticonAdapter(
    private val requestManager: RequestManager,
    private val onMapGifticonListener: OnMapGifticonListener,
) : ListAdapter<MapGifticonItem, MapGifticonViewHolder>(MapGifticonDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapGifticonViewHolder {
        return MapGifticonViewHolder(parent, requestManager, onMapGifticonListener)
    }

    override fun onBindViewHolder(holder: MapGifticonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}