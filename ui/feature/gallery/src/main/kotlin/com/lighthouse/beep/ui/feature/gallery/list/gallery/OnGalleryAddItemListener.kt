package com.lighthouse.beep.ui.feature.gallery.list.gallery

import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem

internal interface OnGalleryAddItemListener {

    fun onClick(item: GalleryItem.AddItem)
}