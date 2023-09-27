package com.lighthouse.beep.navs

import com.lighthouse.beep.model.gallery.GalleryImage

sealed interface ActivityNavItem {

    data object Login : ActivityNavItem

    data object Home : ActivityNavItem

    data object Gallery : ActivityNavItem

    data class Editor(
        val list: List<GalleryImage> = emptyList()
    ) : ActivityNavItem
}