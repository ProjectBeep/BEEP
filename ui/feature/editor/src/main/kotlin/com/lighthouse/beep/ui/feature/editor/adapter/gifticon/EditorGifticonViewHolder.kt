package com.lighthouse.beep.ui.feature.editor.adapter.gifticon

import android.graphics.RectF
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.size.Size
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorGifticonBinding

internal class EditorGifticonViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorGifticonListener,
    private val binding: ItemEditorGifticonBinding = ItemEditorGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<GalleryImage>(binding.root) {

    companion object {
        private val VIEW_RECT = RectF(0f, 0f, 48f.dp, 48f.dp)
    }

    init {
        binding.imageGifticon.clipToOutline = true
    }

    override fun bind(item: GalleryImage) {
        super.bind(item)


        binding.imageGifticon.load(data = item.contentUri) {
            size(Size.ORIGINAL)
        }
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
            if (data.isCropped) {
                binding.imageGifticon.scaleType = ImageView.ScaleType.MATRIX
                binding.imageGifticon.imageMatrix = data.calculateMatrix(VIEW_RECT)
            } else {
                binding.imageGifticon.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        listener.isInvalidFlow(item).collect(lifecycleOwner) { isInvalid ->
            binding.iconInvalid.isVisible = isInvalid
        }
    }
}