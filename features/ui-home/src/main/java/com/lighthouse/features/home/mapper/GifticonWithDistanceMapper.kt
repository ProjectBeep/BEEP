package com.lighthouse.features.home.mapper

import com.lighthouse.beep.model.gifticon.GifticonWithDistance
import com.lighthouse.common.mapper.toUri
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.core.exts.toFormatString
import com.lighthouse.features.home.R
import com.lighthouse.features.home.model.UIGifticonWithDistance

internal fun GifticonWithDistance.toUIModel(): UIGifticonWithDistance {
    val bodyUIText = when (isCashCard) {
        true -> UIText.StringResource(R.string.card_cash, remainCash)
        false -> UIText.DynamicString(name)
    }

    val dDayUIText = when (dDay < 1000) {
        true -> UIText.StringResource(R.string.card_dday, dDay)
        false -> UIText.DynamicString("999+")
    }

    val distanceUIText = when (distance > 50) {
        true -> UIText.DynamicString("${distance}m")
        false -> UIText.StringResource(R.string.card_near)
    }

    return UIGifticonWithDistance(
        id,
        croppedUri.toUri(),
        bodyUIText,
        brand,
        expireAt.toFormatString("~ yyyy.MM.dd"),
        dDayUIText,
        distanceUIText
    )
}
