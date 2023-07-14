package com.lighthouse.features.home.model

import com.lighthouse.core.android.utils.resource.UIText

internal sealed class HomeItem {

    data class Header(
        val title: UIText,
        val actionText: UIText,
        val action: HomeAction
    ) : HomeItem()

    data class CardList(val card: HomeCard)

    object MapLoading : HomeItem()

    object MapEmpty : HomeItem()
}
