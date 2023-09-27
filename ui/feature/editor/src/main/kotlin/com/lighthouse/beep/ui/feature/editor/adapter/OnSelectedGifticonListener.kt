package com.lighthouse.beep.ui.feature.editor.adapter

import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

internal interface OnSelectedGifticonListener {

    fun isSelectedFlow(item: GalleryImage): Flow<Boolean>

    fun isInvalidFlow(item: GalleryImage): Flow<Boolean>

    fun onClick(item: GalleryImage)

    fun onDeleteClick(item: GalleryImage)
}