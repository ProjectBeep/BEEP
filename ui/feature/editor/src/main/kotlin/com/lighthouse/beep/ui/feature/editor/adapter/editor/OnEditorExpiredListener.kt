package com.lighthouse.beep.ui.feature.editor.adapter.editor

import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface OnEditorExpiredListener {

    fun getGifticonDataFlow(): Flow<GifticonData>

    fun showExpired()
}