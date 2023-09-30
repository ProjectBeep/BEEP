package com.lighthouse.beep.ui.feature.editor.adapter.editor

import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import kotlinx.coroutines.flow.Flow

internal interface OnEditorTextListener {

    fun getTextFlow(item: EditorChip.Property): Flow<String>

    fun onEditClick(item: EditorChip.Property)
}