package com.lighthouse.beep.ui.feature.editor.adapter.preview

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface OnEditorPreviewListener {

    fun getGifticonDataFlow(image: GalleryImage): Flow<GifticonData>

    fun onCashChange(item: GalleryImage, isCash: Boolean)

    fun onEditClick(editType: EditType)
}