package com.lighthouse.beep.ui.feature.editor.adapter.editor

import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail
import kotlinx.coroutines.flow.Flow

internal interface OnEditorThumbnailListener {

    fun getThumbnailFlow(): Flow<EditGifticonThumbnail>

    fun showBuiltInThumbnail()

    fun clearThumbnail()
}