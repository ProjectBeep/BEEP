package com.lighthouse.beep.ui.feature.gallery.list.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.databinding.ItemGalleryBinding

internal class GalleryViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnGalleryListener,
    private val binding: ItemGalleryBinding = ItemGalleryBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<GalleryImage>(binding.root) {

    override fun bind(item: GalleryImage) {
        super.bind(item)

        requestManager.load(item.contentUri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageGallery)
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GalleryImage) {
        listener.getSelectedIndexFlow(item).collect(lifecycleOwner, default = -1) { index ->
            val isSelected = index != -1
            binding.textSelectedOrder.isVisible = isSelected
            binding.textSelectedOrder.text = (index + 1).toString()
            binding.imageGallery.isSelected = isSelected
        }

        listener.getAddedGifticonFlow(item).collect(
            lifecycleOwner = lifecycleOwner,
            defaultBlock = {
                binding.viewAddedGifticon.isVisible = false
            },
            block = { isAdded ->
                binding.viewAddedGifticon.isVisible = isAdded
            },
        )
    }
}