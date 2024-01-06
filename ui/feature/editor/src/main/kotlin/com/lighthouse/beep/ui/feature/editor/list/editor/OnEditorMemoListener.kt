package com.lighthouse.beep.ui.feature.editor.list.editor

import kotlinx.coroutines.flow.Flow

internal interface OnEditorMemoListener {

    fun getMemoFlow() : Flow<String>

    fun onMemoClick()
}