package com.lighthouse.beep.ui.feature.editor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.databinding.ItemSelectedGifticonBinding

internal class SelectedGifticonViewHolder(
    parent: ViewGroup,
    private val listener: OnSelectedGifticonListener,
    private val binding: ItemSelectedGifticonBinding = ItemSelectedGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<GalleryImage>(binding.root) {

    override fun bind(item: GalleryImage) {
        super.bind(item)

        binding.imageGifticon.load(item.contentUri) {
            transformations(RoundedCornersTransformation(8f.dp))
        }
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }

        binding.btnDelete.setOnThrottleClickListener {
            listener.onDeleteClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GalleryImage) {
        listener.isSelectedFlow(item).collect(lifecycleOwner) { isSelected ->
            binding.imageGifticon.isSelected = isSelected
        }

        listener.isInvalidFlow(item).collect(lifecycleOwner) { isInvalid ->
            binding.iconInvalid.isVisible = isInvalid
        }
    }
}