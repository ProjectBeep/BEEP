package com.lighthouse.beep.ui.feature.home.model

internal data class GifticonQuery(
    val userUid: String = "",
    val order: GifticonOrder = GifticonOrder.D_DAY,
    val brandItem: BrandItem = BrandItem.All,
) {
    companion object {
        val Default = GifticonQuery()
    }
}