package com.lighthouse.beep.ui.feature.gallery.adapter.selected

import com.lighthouse.beep.model.gallery.GalleryImage

internal interface OnSelectedGalleryListener {

    fun onClick(item: GalleryImage)
}