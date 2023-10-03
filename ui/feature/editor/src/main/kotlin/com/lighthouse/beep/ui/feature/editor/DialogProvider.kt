package com.lighthouse.beep.ui.feature.editor

import com.lighthouse.beep.ui.feature.editor.model.EditType

internal interface DialogProvider {
    fun showTextInputDialog(type: EditType)
}