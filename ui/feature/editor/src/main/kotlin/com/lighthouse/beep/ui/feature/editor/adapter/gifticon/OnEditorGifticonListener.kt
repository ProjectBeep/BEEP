package com.lighthouse.beep.ui.feature.editor.adapter.gifticon

import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail
import kotlinx.coroutines.flow.Flow

internal interface OnEditorGifticonListener {

    fun isSelectedFlow(item: GalleryImage): Flow<Boolean>

    fun isInvalidFlow(item: GalleryImage): Flow<Boolean>

    fun getCropDataFlow(item: GalleryImage): Flow<EditGifticonThumbnail>

    fun onClick(item: GalleryImage, position: Int)

    fun onDeleteClick(item: GalleryImage)
}