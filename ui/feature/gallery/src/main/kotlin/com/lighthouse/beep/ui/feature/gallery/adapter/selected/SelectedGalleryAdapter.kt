package com.lighthouse.beep.ui.feature.gallery.adapter.selected

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.GalleryImageDiff

internal class SelectedGalleryAdapter(
    private val onSelectedGalleryListener: OnSelectedGalleryListener,
) : ListAdapter<GalleryImage, SelectedGalleryViewHolder>(GalleryImageDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedGalleryViewHolder {
        return SelectedGalleryViewHolder(parent, onSelectedGalleryListener)
    }

    override fun onBindViewHolder(holder: SelectedGalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}