package com.lighthouse.navs.main.model

sealed class MainNavigationItem {

    object Popup : MainNavigationItem()

    object GifticonList : MainNavigationItem()

    object Home : MainNavigationItem()

    object Setting : MainNavigationItem()
}
