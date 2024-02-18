package com.lighthouse.beep.ui.feature.archive.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.archive.model.Diff
import com.lighthouse.beep.ui.feature.archive.model.UsedGifticonItem

internal class UsedGifticonAdapter(
    private val getRequestManager: () -> RequestManager,
    private val usedGifticonListener: UsedGifticonListener,
): ListAdapter<UsedGifticonItem, UsedGifticonViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsedGifticonViewHolder {
        return UsedGifticonViewHolder(parent, getRequestManager(), usedGifticonListener)
    }

    override fun onBindViewHolder(holder: UsedGifticonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}