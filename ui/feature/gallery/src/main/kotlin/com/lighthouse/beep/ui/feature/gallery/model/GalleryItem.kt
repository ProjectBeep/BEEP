package com.lighthouse.beep.ui.feature.gallery.model

import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.gallery.GalleryImage

internal sealed interface GalleryItem {

    data object AddItem : GalleryItem

    data class Image(val item: GalleryImage): GalleryItem
}

internal class GalleryItemDiff : DiffUtil.ItemCallback<GalleryItem>() {

    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return when {
            oldItem is GalleryItem.AddItem && newItem is GalleryItem.AddItem -> true
            oldItem is GalleryItem.Image && newItem is GalleryItem.Image -> oldItem.item.id == newItem.item.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return when {
            oldItem is GalleryItem.AddItem && newItem is GalleryItem.AddItem -> true
            oldItem is GalleryItem.Image && newItem is GalleryItem.Image -> oldItem.item == newItem.item
            else -> false
        }
    }
}