package com.lighthouse.beep.ui.feature.editor.adapter.editor

import com.lighthouse.beep.ui.feature.editor.model.PropertyType
import kotlinx.coroutines.flow.Flow

internal interface OnEditorPreviewListener {

    fun getInvalidPropertyFlow() : Flow<List<PropertyType>>
}