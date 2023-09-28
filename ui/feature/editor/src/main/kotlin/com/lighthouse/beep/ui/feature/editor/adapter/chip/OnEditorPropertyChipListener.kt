package com.lighthouse.beep.ui.feature.editor.adapter.chip

import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import kotlinx.coroutines.flow.Flow

internal interface OnEditorPropertyChipListener {

    fun isSelectedFlow(item: EditorChip.Property): Flow<Boolean>

    fun isInvalidFlow(item: EditorChip.Property): Flow<Boolean>

    fun onClick(item: EditorChip.Property, position: Int)
}