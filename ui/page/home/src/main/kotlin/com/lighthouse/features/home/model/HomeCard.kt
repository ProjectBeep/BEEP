package com.lighthouse.features.home.model

import com.lighthouse.beep.model.gifticon.Gifticon
import com.lighthouse.beep.model.gifticon.GifticonWithDistance

internal sealed class HomeCard {

    data class Map(val items: List<GifticonWithDistance>)

    data class Expired(val items: List<Gifticon>)
}
