package com.lighthouse.beep.ui.feature.gallery.list.gallery

import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem
import kotlinx.coroutines.flow.Flow

internal interface OnGalleryItemListener {

    fun getSelectedIndexFlow(item: GalleryItem.Image): Flow<Int>

    fun getAddedGifticonFlow(item: GalleryItem.Image): Flow<Boolean>

    fun onClick(item: GalleryItem.Image)
}