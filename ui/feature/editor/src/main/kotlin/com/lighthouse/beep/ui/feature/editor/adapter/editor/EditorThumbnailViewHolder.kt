package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.GifticonThumbnail
import com.lighthouse.beep.ui.feature.editor.model.loadThumbnail

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
            requestManager.loadThumbnail(data)
                .into(binding.imageThumbnail)

            when (data) {
                is GifticonThumbnail.BuiltIn -> {
                    binding.textThumbnailBuiltIn.setText(data.builtIn.titleRes)
                    binding.textThumbnailBuiltIn.setTextColor(getColor(ThemeR.color.font_dark_gray))
                }

                else -> {
                    binding.textThumbnailBuiltIn.setText(R.string.editor_gifticon_preview_thumbnail_built_in)
                    binding.textThumbnailBuiltIn.setTextColor(getColor(ThemeR.color.font_medium_gray))
                }
            }
        }
    }

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.viewShowBuiltInThumbnailDialog.setOnThrottleClickListener {
            listener.showBuiltInThumbnail()
        }
    }
}