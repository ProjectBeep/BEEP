package com.lighthouse.beep.navs

import com.lighthouse.beep.model.gallery.GalleryImage

sealed interface ActivityNavItem {

    data class Login(
        val clearTask: Boolean = false
    ) : ActivityNavItem

    data object Home : ActivityNavItem

    data object Gallery : ActivityNavItem

    data class Register(
        val list: List<GalleryImage> = emptyList()
    ) : ActivityNavItem

    data class Edit(
        val gifticonId: Long = -1
    ) : ActivityNavItem

    data object Archive : ActivityNavItem

    data object Setting : ActivityNavItem
}