package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.flow.Flow

internal interface OnGalleryListener {

    fun getSelectedIndexFlow(item: GalleryImage): Flow<Int>

    fun onClick(item: GalleryImage)
}