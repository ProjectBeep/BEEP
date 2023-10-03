package com.lighthouse.beep.ui.feature.editor

import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal interface OnEditorChipListener {
    fun selectEditorChip(item: EditorChip)

    fun selectEditorChip(type: EditType)
}