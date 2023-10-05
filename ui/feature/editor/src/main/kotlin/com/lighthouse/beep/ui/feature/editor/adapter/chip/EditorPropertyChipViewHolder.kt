package com.lighthouse.beep.ui.feature.editor.adapter.chip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorPropertyChipBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorPropertyChipViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorPropertyChipListener,
    private val binding: ItemEditorPropertyChipBinding = ItemEditorPropertyChipBinding.inflate(
        LayoutInflater.from(parent.context)
    )
) : LifecycleViewHolder<EditorChip.Property>(binding.root){

    override fun bind(item: EditorChip.Property) {
        super.bind(item)

        binding.chip.setText(item.type.textResId)
    }

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item, absoluteAdapterPosition)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.isSelectedFlow(item).collect(lifecycleOwner) { isSelected ->
            binding.root.isSelected = isSelected
            binding.chip.isSelected = isSelected
        }

        listener.isInvalidFlow(item.type).collect(lifecycleOwner) { isInvalid ->
            binding.iconInvalid.isVisible = isInvalid
        }
    }
}