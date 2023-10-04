package com.lighthouse.beep.ui.feature.editor.adapter.editor

import kotlinx.coroutines.flow.Flow

internal interface OnEditorPreviewListener {

    fun getMemoFlow() : Flow<String>

    fun onMemoClick()
}