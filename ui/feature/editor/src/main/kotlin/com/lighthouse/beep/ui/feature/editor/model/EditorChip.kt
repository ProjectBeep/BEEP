package com.lighthouse.beep.ui.feature.editor.model

import androidx.recyclerview.widget.DiffUtil


internal sealed interface EditorChip {

    data object Preview : EditorChip

    data class Property(val type: PropertyType) : EditorChip
}

internal class EditorChipDiff : DiffUtil.ItemCallback<EditorChip>() {
    override fun areItemsTheSame(
        oldItem: EditorChip,
        newItem: EditorChip,
    ): Boolean {
        return when {
            oldItem is EditorChip.Preview && newItem is EditorChip.Preview -> oldItem == newItem
            oldItem is EditorChip.Property && newItem is EditorChip.Property -> oldItem.type == newItem.type
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: EditorChip,
        newItem: EditorChip,
    ): Boolean {
        return oldItem == newItem
    }
}

internal class EditorChipPropertyDiff : DiffUtil.ItemCallback<EditorChip.Property>() {
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