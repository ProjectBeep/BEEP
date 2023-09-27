package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.GalleryImageDiff

internal class GalleryAllAdapter(
    private val onGalleryListener: OnGalleryListener,
) : PagingDataAdapter<GalleryImage, GalleryViewHolder>(GalleryImageDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(parent, onGalleryListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }
}