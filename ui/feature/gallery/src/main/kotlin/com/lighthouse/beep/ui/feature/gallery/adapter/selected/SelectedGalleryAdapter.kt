package com.lighthouse.beep.ui.feature.gallery.adapter.selected

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageDiff

internal class SelectedGalleryAdapter(
    private val requestManager: RequestManager,
    private val onSelectedGalleryListener: OnSelectedGalleryListener,
) : ListAdapter<GalleryImage, SelectedGalleryViewHolder>(GalleryImageDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedGalleryViewHolder {
        return SelectedGalleryViewHolder(
            parent,
            requestManager,
            onSelectedGalleryListener
        )
    }

    override fun onBindViewHolder(holder: SelectedGalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}