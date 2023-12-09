package com.lighthouse.beep.ui.feature.editor.dialog.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnailDiff

internal class BuiltInThumbnailAdapter(
    private val listener: OnBuiltInThumbnailListener,
) : ListAdapter<GifticonBuiltInThumbnail, BuiltInThumbnailViewHolder>(GifticonBuiltInThumbnailDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuiltInThumbnailViewHolder {
        return BuiltInThumbnailViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: BuiltInThumbnailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}