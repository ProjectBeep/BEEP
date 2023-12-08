package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.graphics.RectF
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorThumbnailViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnEditorThumbnailListener,
    private val binding: SectionEditorThumbnailBinding = SectionEditorThumbnailBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<EditorChip.Property>(binding.root){

    companion object {
        private val VIEW_RECT = RectF(0f, 0f, 50f.dp, 50f.dp)
    }

    init {
        binding.imageThumbnail.clipToOutline = true
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getThumbnailFlow().collect(lifecycleOwner) { uri ->
            requestManager.load(uri)
                .into(binding.imageThumbnail)
        }

        listener.getCropDataFlow().collect(lifecycleOwner) { data ->
            if (data.isCropped) {
                binding.imageThumbnail.scaleType = ImageView.ScaleType.MATRIX
                binding.imageThumbnail.imageMatrix = data.calculateMatrix(VIEW_RECT)
            } else {
                binding.imageThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }
}