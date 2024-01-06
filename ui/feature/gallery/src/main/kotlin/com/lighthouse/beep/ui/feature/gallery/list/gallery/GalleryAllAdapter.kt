package com.lighthouse.beep.ui.feature.gallery.list.gallery

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageDiff

internal class GalleryAllAdapter(
    private val requestManager: RequestManager,
    private val onGalleryListener: OnGalleryListener,
) : PagingDataAdapter<GalleryImage, GalleryViewHolder>(GalleryImageDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(parent, requestManager, onGalleryListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    fun getItemByPosition(position: Int): GalleryImage? {
        if (position == -1) {
            return null
        }
        return getItem(position)
    }
}