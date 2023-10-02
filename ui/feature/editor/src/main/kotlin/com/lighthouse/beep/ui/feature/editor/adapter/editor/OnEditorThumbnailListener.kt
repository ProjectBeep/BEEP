package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.graphics.RectF
import android.net.Uri
import kotlinx.coroutines.flow.Flow

internal interface OnEditorThumbnailListener {

    fun getThumbnailFlow(): Flow<Uri>

    fun getCropRectFlow(): Flow<RectF>

    fun isThumbnailEditedFlow(): Flow<Boolean>
}