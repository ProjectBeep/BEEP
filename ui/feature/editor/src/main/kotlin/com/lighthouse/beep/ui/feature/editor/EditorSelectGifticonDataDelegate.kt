package com.lighthouse.beep.ui.feature.editor

import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface EditorSelectGifticonDataDelegate {

    val selectedGifticonDataFlow: Flow<GifticonData>
}