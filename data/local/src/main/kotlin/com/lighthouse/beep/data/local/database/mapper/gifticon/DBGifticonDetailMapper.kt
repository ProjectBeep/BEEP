package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.model.DBGifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonBarcodeType
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonThumbnail

internal fun DBGifticonDetail.toModel(): GifticonDetail {
    return GifticonDetail(
        id = id,
        userId = "", // 새 모델에서는 Item 레벨에서 관리
        isCashCard = isCashCard,
        remainCash = remainCash ?: 0,
        totalCash = totalCash ?: 0,
        thumbnail = GifticonThumbnail.BuildIn(""), // 새 모델에서는 별도 이미지 테이블 사용
        name = name,
        displayBrand = displayBrand,
        barcodeType = GifticonBarcodeType.of(codeType),
        barcode = code,
        memo = memo ?: "",
        isUsed = isUsed,
        expireAt = expireAt,
    )
}
