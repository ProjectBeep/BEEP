package com.lighthouse.beep.ui.feature.home.model

internal data class BrandScrollInfo(
    val position: Int,
    val offset: Int
) {
    companion object {
        val None = BrandScrollInfo(0, 0)
    }
}