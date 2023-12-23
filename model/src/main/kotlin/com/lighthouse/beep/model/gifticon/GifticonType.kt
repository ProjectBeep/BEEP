package com.lighthouse.beep.model.gifticon

sealed interface GifticonType {

    data object Product : GifticonType
    data class Cash(
        val remain: Int,
        val total: Int,
    ) : GifticonType
}