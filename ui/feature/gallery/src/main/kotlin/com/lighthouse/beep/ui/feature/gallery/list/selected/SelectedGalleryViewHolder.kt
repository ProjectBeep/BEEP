package com.lighthouse.beep.ui.feature.gallery.list.selected

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.databinding.ItemSelectedGalleryBinding

internal class SelectedGalleryViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnSelectedGalleryListener,
    private val binding: ItemSelectedGalleryBinding = ItemSelectedGalleryBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.imageGallery.clipToOutline = true
    }

    fun bind(item: GalleryImage) {
        requestManager.load(item.contentUri)
            .into(binding.imageGallery)

        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }
}