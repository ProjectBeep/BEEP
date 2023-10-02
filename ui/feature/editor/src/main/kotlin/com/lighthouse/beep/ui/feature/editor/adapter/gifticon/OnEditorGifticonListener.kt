package com.lighthouse.beep.ui.feature.editor.adapter.gifticon

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.CropData
import kotlinx.coroutines.flow.Flow

internal interface OnEditorGifticonListener {

    fun isSelectedFlow(item: GalleryImage): Flow<Boolean>

    fun isInvalidFlow(item: GalleryImage): Flow<Boolean>

    fun getCropDataFlow(item: GalleryImage): Flow<CropData>

    fun onClick(item: GalleryImage)

    fun onDeleteClick(item: GalleryImage)
}