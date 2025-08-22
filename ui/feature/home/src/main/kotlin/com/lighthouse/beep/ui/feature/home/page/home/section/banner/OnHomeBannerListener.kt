package com.lighthouse.beep.ui.feature.home.page.home.section.banner

import com.lighthouse.beep.ui.feature.home.model.HomeBannerItem

internal interface OnHomeBannerListener {

    fun onClick(item: HomeBannerItem)
}