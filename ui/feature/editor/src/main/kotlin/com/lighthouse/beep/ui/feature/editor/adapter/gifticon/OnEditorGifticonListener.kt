package com.lighthouse.beep.ui.feature.editor.adapter.gifticon

import android.graphics.RectF
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

internal interface OnEditorGifticonListener {

    fun isSelectedFlow(item: GalleryImage): Flow<Boolean>

    fun isInvalidFlow(item: GalleryImage): Flow<Boolean>

    fun getCropRectFlow(item: GalleryImage): Flow<RectF>

    fun onClick(item: GalleryImage)

    fun onDeleteClick(item: GalleryImage)
}