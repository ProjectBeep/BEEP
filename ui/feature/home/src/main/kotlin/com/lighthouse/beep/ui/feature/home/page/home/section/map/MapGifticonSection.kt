package com.lighthouse.beep.ui.feature.home.page.home.section.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.databinding.SectionMapGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.HomeItem

internal class MapGifticonSection(
    parent: ViewGroup,
    private val listener: OnMapGifticonSectionListener,
    private val onMapGifticonListener: OnMapGifticonListener,
    private val binding: SectionMapGifticonBinding = SectionMapGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<HomeItem.MapGifticon>(binding.root) {

    private val mapGifticonAdapter = MapGifticonAdapter(onMapGifticonListener)

    init {
        binding.listMapGifticon.adapter = mapGifticonAdapter
        binding.btnGotoMap.setOnThrottleClickListener {
            listener.onGotoMapClick()
        }
    }


    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.MapGifticon) {
        listener.getMapGifticonListFlow().collect(lifecycleOwner) { list ->
            mapGifticonAdapter.submitList(list)
        }
    }
}