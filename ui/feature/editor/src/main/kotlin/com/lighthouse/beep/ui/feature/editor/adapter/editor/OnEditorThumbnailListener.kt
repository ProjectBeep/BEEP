package com.lighthouse.beep.ui.feature.editor.adapter.editor

import com.lighthouse.beep.ui.feature.editor.model.GifticonThumbnail
import kotlinx.coroutines.flow.Flow

internal interface OnEditorThumbnailListener {

    fun getThumbnailFlow(): Flow<GifticonThumbnail>

    fun showBuiltInThumbnail()
}