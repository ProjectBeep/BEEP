package com.lighthouse.beep.ui.feature.home.decorator

internal interface HomeItemDecorationCallback {

    fun onTopItemPosition(position: Int)

    fun getExpiredGifticonFirstIndex(): Int
}