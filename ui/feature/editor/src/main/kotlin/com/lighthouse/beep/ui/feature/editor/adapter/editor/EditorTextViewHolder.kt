package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorTextBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorTextViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorTextListener,
    private val binding: SectionEditorTextBinding = SectionEditorTextBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<EditorChip.Property>(binding.root) {

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.textEditor.setOnThrottleClickListener {
            listener.onEditClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getTextFlow(item).collect(lifecycleOwner) { text ->
            binding.textEditor.text = text
        }
    }
}