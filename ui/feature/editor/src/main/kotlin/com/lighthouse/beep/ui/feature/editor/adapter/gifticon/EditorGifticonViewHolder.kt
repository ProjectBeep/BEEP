package com.lighthouse.beep.ui.feature.editor.adapter.gifticon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorGifticonBinding
import com.lighthouse.beep.ui.feature.editor.model.loadThumbnail

internal class EditorGifticonViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnEditorGifticonListener,
    private val binding: ItemEditorGifticonBinding = ItemEditorGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<GalleryImage>(binding.root) {

    init {
        binding.imageGifticon.clipToOutline = true
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item, absoluteAdapterPosition)
        }

        binding.btnDelete.setOnThrottleClickListener {
            listener.onDeleteClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GalleryImage) {
        listener.isSelectedFlow(item).collect(lifecycleOwner) { isSelected ->
            binding.imageGifticon.isSelected = isSelected
        }

        listener.getCropDataFlow(item).collect(lifecycleOwner) { data ->
            requestManager.loadThumbnail(data)
                .into(binding.imageGifticon)
        }

        listener.isInvalidFlow(item).collect(lifecycleOwner) { isInvalid ->
            binding.iconInvalid.isVisible = isInvalid
        }
    }
}