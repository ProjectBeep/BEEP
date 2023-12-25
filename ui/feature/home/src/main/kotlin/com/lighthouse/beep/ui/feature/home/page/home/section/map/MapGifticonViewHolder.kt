package com.lighthouse.beep.ui.feature.home.page.home.section.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.databinding.ItemMapGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem

internal class MapGifticonViewHolder(
    parent: ViewGroup,
    private val listener: OnMapGifticonListener,
    private val binding: ItemMapGifticonBinding = ItemMapGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<MapGifticonItem>(binding.root) {

    override fun bind(item: MapGifticonItem) {
        super.bind(item)

        binding.textGifticonName.text = item.name
        binding.textBrand.text = item.name
    }

    override fun onSetUpClickEvent(item: MapGifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: MapGifticonItem) {
        listener.getCurrentDmsPosFlow().collect(lifecycleOwner) { current ->
            binding.textDistance.text = "${item.getDistance(current)}m"
        }
    }
}