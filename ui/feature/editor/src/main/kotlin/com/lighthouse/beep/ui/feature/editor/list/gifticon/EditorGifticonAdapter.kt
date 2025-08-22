package com.lighthouse.beep.ui.feature.editor.list.gifticon

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageDiff

internal class EditorGifticonAdapter(
    private val requestManager: RequestManager,
    private val onSelectedGalleryListener: OnEditorGifticonListener,
) : ListAdapter<GalleryImage, EditorGifticonViewHolder>(GalleryImageDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorGifticonViewHolder {
        return EditorGifticonViewHolder(
            parent,
            requestManager,
            onSelectedGalleryListener,
        )
    }

    override fun onBindViewHolder(holder: EditorGifticonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}