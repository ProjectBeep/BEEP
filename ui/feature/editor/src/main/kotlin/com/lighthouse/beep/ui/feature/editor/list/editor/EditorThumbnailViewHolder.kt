package com.lighthouse.beep.ui.feature.editor.list.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail

internal class EditorThumbnailViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnEditorThumbnailListener,
    private val binding: SectionEditorThumbnailBinding = SectionEditorThumbnailBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<EditorChip.Property>(binding.root) {

    init {
        binding.imageThumbnail.clipToOutline = true
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getThumbnailFlow().collect(lifecycleOwner) { data ->
            binding.groupRecommend.isVisible = data !is EditGifticonThumbnail.BuiltIn
            binding.groupThumbnail.isVisible = data is EditGifticonThumbnail.BuiltIn

            when (data) {
                is EditGifticonThumbnail.Default -> {
                    requestManager
                        .load(data.thumbnailUri ?: data.originUri)
                        .transform(CenterCrop())
                        .into(binding.imageThumbnail)
                }

                is EditGifticonThumbnail.Crop -> {
                    requestManager
                        .load(data.bitmap)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.imageThumbnail)
                }

                is EditGifticonThumbnail.BuiltIn -> {
                    requestManager
                        .load(data.builtIn.smallIconRes)
                        .into(binding.imageThumbnail)
                }
            }

            if (data is EditGifticonThumbnail.BuiltIn) {
                binding.textThumbnail.setText(data.builtIn.titleRes)
            }
        }
    }

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.containerThumbnailInfo.setOnThrottleClickListener {
            listener.showBuiltInThumbnail()
        }

        binding.iconClear.setOnThrottleClickListener {
            listener.clearThumbnail()
        }
    }
}