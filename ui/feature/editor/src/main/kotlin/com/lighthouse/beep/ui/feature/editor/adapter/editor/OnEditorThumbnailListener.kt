package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.net.Uri
import com.lighthouse.beep.ui.feature.editor.model.GifticonThumbnail
import kotlinx.coroutines.flow.Flow

internal interface OnEditorThumbnailListener {

    fun getThumbnailFlow(): Flow<Uri>

    fun getCropDataFlow(): Flow<GifticonThumbnail>
}