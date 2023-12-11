package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorTextBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorTextViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorTextListener,
    private val binding: SectionEditorTextBinding = SectionEditorTextBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<EditorChip.Property>(binding.root) {

    override fun bind(item: EditorChip.Property) {
        super.bind(item)

        binding.textEditor.setHint(item.type.hintResId)
    }

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.textEditor.setOnThrottleClickListener {
            listener.onEditClick(item.type)
        }

        binding.iconClear.setOnThrottleClickListener {
            listener.onClearClick(item.type)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getTextFlow(item.type).collect(lifecycleOwner) { text ->
            binding.iconClear.isVisible = text.isNotEmpty()
            binding.textEditor.text = text
        }
    }
}