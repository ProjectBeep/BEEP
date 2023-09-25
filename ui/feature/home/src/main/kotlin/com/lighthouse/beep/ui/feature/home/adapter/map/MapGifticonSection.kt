package com.lighthouse.beep.ui.feature.home.adapter.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.feature.home.databinding.SectionMapGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.HomeItem

internal class MapGifticonSection(
    parent: ViewGroup,
    private val onMapGifticonListener: OnMapGifticonListener,
    private val binding: SectionMapGifticonBinding = SectionMapGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    private val mapGifticonAdapter = MapGifticonAdapter(onMapGifticonListener)

    init {
        binding.listMapGifticon.adapter = mapGifticonAdapter
        binding.btnGotoMap.setOnThrottleClickListener {
            onMapGifticonListener.onGotoMapClick()
        }
    }

    fun bind(item: HomeItem.MapGifticon) {
        mapGifticonAdapter.submitList(item.list)
    }
}