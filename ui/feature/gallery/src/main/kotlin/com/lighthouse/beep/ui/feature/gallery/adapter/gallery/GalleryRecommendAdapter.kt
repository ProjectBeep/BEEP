package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.GalleryImageDiff

internal class GalleryRecommendAdapter : ListAdapter<GalleryImage, GalleryViewHolder>(GalleryImageDiff()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}