package com.lighthouse.beep.ui.feature.gallery.adapter.selected

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.databinding.ItemSelectedGalleryBinding

internal class SelectedGalleryViewHolder(
    parent: ViewGroup,
    private val listener: OnSelectedGalleryListener,
    private val binding: ItemSelectedGalleryBinding = ItemSelectedGalleryBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GalleryImage) {
        binding.imageGallery.load(item.contentUri) {
            transformations(RoundedCornersTransformation(8f.dp))
        }
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }
}