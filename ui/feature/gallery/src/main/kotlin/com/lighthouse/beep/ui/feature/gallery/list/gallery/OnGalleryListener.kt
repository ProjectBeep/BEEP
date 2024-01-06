package com.lighthouse.beep.ui.feature.gallery.list.gallery

import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

internal interface OnGalleryListener {

    fun getSelectedIndexFlow(item: GalleryImage): Flow<Int>

    fun getAddedGifticonFlow(item: GalleryImage): Flow<Boolean>

    fun onClick(item: GalleryImage)
}