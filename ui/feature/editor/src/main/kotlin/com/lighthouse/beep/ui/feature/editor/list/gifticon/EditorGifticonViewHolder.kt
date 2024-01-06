package com.lighthouse.beep.ui.feature.editor.list.gifticon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorGifticonBinding
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail

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
            when (data) {
                is EditGifticonThumbnail.Default -> {
                    requestManager
                        .load(data.originUri)
                        .transform(CenterCrop())
                        .into(binding.imageGifticon)
                }

                is EditGifticonThumbnail.Crop -> {
                    requestManager
                        .load(data.bitmap)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.imageGifticon)
                }

                is EditGifticonThumbnail.BuiltIn -> {
                    requestManager
                        .load(data.builtIn.smallIconRes)
                        .into(binding.imageGifticon)
                }
            }
        }

        listener.isInvalidFlow(item).collect(lifecycleOwner) { isInvalid ->
            binding.iconInvalid.isVisible = isInvalid
        }
    }
}