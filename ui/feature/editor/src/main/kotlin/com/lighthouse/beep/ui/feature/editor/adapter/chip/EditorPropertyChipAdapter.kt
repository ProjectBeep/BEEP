package com.lighthouse.beep.ui.feature.editor.adapter.chip

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorChipPropertyDiff

internal class EditorPropertyChipAdapter(
    private val onEditorPropertyChipListener: OnEditorPropertyChipListener,
) : ListAdapter<EditorChip.Property, EditorPropertyChipViewHolder>(EditorChipPropertyDiff()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditorPropertyChipViewHolder {
        return EditorPropertyChipViewHolder(parent, onEditorPropertyChipListener)
    }

    override fun onBindViewHolder(holder: EditorPropertyChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}