package com.lighthouse.beep.ui.feature.editor.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.ui.feature.editor.databinding.ItemBuiltInThumbnailBinding

internal class BuiltInThumbnailViewHolder(
    parent: ViewGroup,
    private val listener: OnBuiltInThumbnailListener,
    private val binding: ItemBuiltInThumbnailBinding = ItemBuiltInThumbnailBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
): LifecycleViewHolder<GifticonBuiltInThumbnail>(binding.root) {

    init {
        parent.clipChildren = false
    }

    override fun bind(item: GifticonBuiltInThumbnail) {
        super.bind(item)

        binding.iconThumbnail.setImageResource(item.iconRes)
        binding.textTitle.setText(item.titleRes)

        val bgRes = listener.getBackgroundRes(absoluteAdapterPosition)
        binding.view.setBackgroundResource(bgRes)
    }

    override fun onSetUpClickEvent(item: GifticonBuiltInThumbnail) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GifticonBuiltInThumbnail) {
        listener.isSelectedFlow(item).collect(lifecycleOwner) { isSelected ->
            binding.view.isSelected = isSelected
            binding.root.z = if (isSelected) 0.1f else 0f
        }
    }
}