package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorThumbnailViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorThumbnailListener,
    private val binding: SectionEditorThumbnailBinding = SectionEditorThumbnailBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<EditorChip.Property>(binding.root){

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getThumbnailFlow().collect(lifecycleOwner) { uri ->
            binding.imageThumbnail.load(uri) {
                transformations(RoundedCornersTransformation(8f.dp))
            }
        }

        listener.isInvalidThumbnailFlow().collect(lifecycleOwner) { isInvalid ->
            binding.textCheckThumbnail.isVisible = isInvalid
        }
    }
}