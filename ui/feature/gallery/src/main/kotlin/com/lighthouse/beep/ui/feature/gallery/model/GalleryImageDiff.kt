package com.lighthouse.beep.ui.feature.gallery.model

import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.gallery.GalleryImage

internal class GalleryImageDiff : DiffUtil.ItemCallback<GalleryImage>() {

    override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
        return oldItem == newItem
    }
}