package com.lighthouse.beep.ui.feature.editor.model

import androidx.recyclerview.widget.DiffUtil


internal sealed interface EditorChip {

    data object Preview : EditorChip

    data class Property(val type: PropertyType) : EditorChip
}

internal class EditorChipDiff : DiffUtil.ItemCallback<EditorChip.Property>() {
    override fun areItemsTheSame(
        oldItem: EditorChip.Property,
        newItem: EditorChip.Property
    ): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: EditorChip.Property,
        newItem: EditorChip.Property
    ): Boolean {
        return oldItem.type == newItem.type
    }
}