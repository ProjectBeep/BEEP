package com.lighthouse.beep.ui.feature.gallery.model

internal data class GalleryScrollInfo(
    val position: Int,
    val offset: Int,
) {
    companion object {
        val None = GalleryScrollInfo(0, 0)
    }
}