package com.lighthouse.beep.ui.feature.editor.list.preview

import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface OnEditorPreviewListener {

    fun getGifticonDataFlow(id: Long): Flow<GifticonData>

    fun onCashChange(id: Long, isCash: Boolean)

    fun onEditClick(editType: EditType)
}