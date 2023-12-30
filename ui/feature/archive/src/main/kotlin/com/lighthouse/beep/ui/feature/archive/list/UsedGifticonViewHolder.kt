package com.lighthouse.beep.ui.feature.archive.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.ui.feature.archive.databinding.ItemUsedGifticonBinding
import com.lighthouse.beep.ui.feature.archive.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.archive.model.UsedGifticonItem

internal class UsedGifticonViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener:UsedGifticonListener,
    private val binding: ItemUsedGifticonBinding = ItemUsedGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<UsedGifticonItem>(binding.root) {

    init {
        binding.root.clipToOutline = true
    }

    override fun bind(item: UsedGifticonItem) {
        super.bind(item)

        when (val thumbnail = item.thumbnail) {
            is GifticonThumbnail.Image -> {
                requestManager
                    .load(thumbnail.uri)
                    .into(binding.imageThumbnail)
            }

            is GifticonThumbnail.BuildIn -> {
                requestManager
                    .load(thumbnail.icon.largeIconRes)
                    .into(binding.imageThumbnail)
            }
        }

        binding.textBrand.text = item.brand
        binding.textName.text = item.name
        binding.textExpire.text = item.formattedExpiredDate
    }

    override fun onSetUpClickEvent(item: UsedGifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: UsedGifticonItem) {
        listener.isSelectedFlow(item).collect(
            lifecycleOwner = lifecycleOwner,
            defaultBlock = {
                binding.btnSelector.isSelected = false
            },
            block = { isSelected ->
                binding.btnSelector.isSelected = isSelected
            }
        )

        listener.getViewModeFlow().collect(lifecycleOwner = lifecycleOwner) { mode ->
            binding.btnSelector.isVisible = mode == GifticonViewMode.EDIT
        }
    }
}