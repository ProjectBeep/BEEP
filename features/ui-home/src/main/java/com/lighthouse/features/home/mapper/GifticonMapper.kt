package com.lighthouse.features.home.mapper

import com.lighthouse.beep.model.gifticon.Gifticon
import com.lighthouse.common.mapper.toUri
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.core.exts.toFormatString
import com.lighthouse.features.home.R
import com.lighthouse.features.home.model.UIGifticon

internal fun Gifticon.toUIModel(): UIGifticon {
    val bodyUIText = when (isCashCard) {
        true -> UIText.StringResource(R.string.card_cash, remainCash)
        false -> UIText.DynamicString(name)
    }

    val dDayUIText = when (dDay < 1000) {
        true -> UIText.StringResource(R.string.card_dday, dDay)
        false -> UIText.DynamicString("999+")
    }

    return UIGifticon(
        id,
        croppedUri.toUri(),
        bodyUIText,
        brand,
        expireAt.toFormatString("~ yyyy.MM.dd"),
        dDayUIText
    )
}
