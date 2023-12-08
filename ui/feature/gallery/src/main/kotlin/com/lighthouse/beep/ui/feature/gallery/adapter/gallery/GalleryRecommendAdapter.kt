package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageDiff

internal class GalleryRecommendAdapter(
    private val requestManager: RequestManager,
    private val onGalleryListener: OnGalleryListener,
) : ListAdapter<GalleryImage, GalleryViewHolder>(GalleryImageDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(parent, requestManager, onGalleryListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemByPosition(position: Int): GalleryImage? {
        if (position == -1) {
            return null
        }
        return getItem(position)
    }
}