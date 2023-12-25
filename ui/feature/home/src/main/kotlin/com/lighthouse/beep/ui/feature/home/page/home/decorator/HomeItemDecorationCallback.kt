package com.lighthouse.beep.ui.feature.home.page.home.decorator

internal interface HomeItemDecorationCallback {

    fun onTopItemPosition(position: Int)

    fun getExpiredGifticonFirstIndex(): Int
}