package com.lighthouse.features.common.model

sealed class NavigationItem {

    object Popup : NavigationItem()

    object UsedGifticon : NavigationItem()

    object Coffee : NavigationItem()

    object OpensourceLicense : NavigationItem()

    object PersonalInfoPolicy : NavigationItem()

    object Security : NavigationItem()

    object TermsOfUse : NavigationItem()
}
