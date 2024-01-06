package com.lighthouse.beep.ui.feature.editor.dialog

import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface BuiltInThumbnailListener {

    val selectedGifticonDataFlow: Flow<GifticonData>

    fun updateGifticonData(editData: EditData)
}