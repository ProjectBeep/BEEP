package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import coil.load
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.databinding.ItemGalleryBinding

internal class GalleryViewHolder(
    parent: ViewGroup,
    private val binding: ItemGalleryBinding = ItemGalleryBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<GalleryImage>(binding.root) {

    override fun bind(item: GalleryImage) {
        super.bind(item)

        binding.imageGallery.load(item.contentUri)
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GalleryImage) {
    }
}