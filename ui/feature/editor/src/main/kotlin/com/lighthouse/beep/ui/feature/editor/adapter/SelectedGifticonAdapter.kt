package com.lighthouse.beep.ui.feature.editor.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageDiff

internal class SelectedGifticonAdapter(
    private val onSelectedGalleryListener: OnSelectedGifticonListener,
) : ListAdapter<GalleryImage, SelectedGifticonViewHolder>(GalleryImageDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedGifticonViewHolder {
        return SelectedGifticonViewHolder(parent, onSelectedGalleryListener)
    }

    override fun onBindViewHolder(holder: SelectedGifticonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}