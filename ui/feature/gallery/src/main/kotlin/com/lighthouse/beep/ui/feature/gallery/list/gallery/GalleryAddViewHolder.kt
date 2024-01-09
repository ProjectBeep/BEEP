package com.lighthouse.beep.ui.feature.gallery.list.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.feature.gallery.databinding.ItemGalleryAddBinding
import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem

internal class GalleryAddViewHolder(
    parent: ViewGroup,
    private val onGalleryAddItemListener: OnGalleryAddItemListener,
    private val binding: ItemGalleryAddBinding = ItemGalleryAddBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    )
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GalleryItem.AddItem) {
        binding.root.setOnThrottleClickListener {
            onGalleryAddItemListener.onClick(item)
        }
    }
}