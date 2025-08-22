package com.lighthouse.beep.core.ui.model

data class ScrollInfo(
    val position: Int,
    val offset: Int,
) {
    companion object {
        val None = ScrollInfo(0, 0)
    }
}