package com.lighthouse.beep.model.gifticon

import java.util.Date

data class Gifticon(
    val id: String,
    val croppedUri: String,
    val name: String,
    val brand: String,
    val expireAt: Date,
    val isCashCard: Boolean,
    val totalCash: Int,
    val remainCash: Int,
    val dDay: Int
)
