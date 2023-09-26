package com.lighthouse.beep.navs

sealed interface ActivityNavItem {

    data object Login : ActivityNavItem

    data object Home : ActivityNavItem

    data object Gallery : ActivityNavItem
}