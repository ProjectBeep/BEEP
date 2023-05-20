package com.lighthouse.navs.app.model

sealed class AppNavigationItem {

    object Popup : AppNavigationItem()

    object UsedGifticon : AppNavigationItem()

    object Coffee : AppNavigationItem()

    object OpensourceLicense : AppNavigationItem()

    object PersonalInfoPolicy : AppNavigationItem()

    object Security : AppNavigationItem()

    object TermsOfUse : AppNavigationItem()
}
