package com.lighthouse.beep.ui.feature.editor

import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface EditorPreviewViewModelDelegate {

    val selectedGifticonDataFlow: Flow<GifticonData>
}