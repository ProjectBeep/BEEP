package com.lighthouse.beep.data.repository.gifticon

import android.graphics.Rect
import android.net.Uri
import com.lighthouse.beep.model.gifticon.GifticonImageCreateResult
import com.lighthouse.beep.model.gifticon.GifticonImageUpdateResult

interface LocalGifticonStorage {

    suspend fun createGifticonImage(
        inputOriginUri: Uri,
        inputCropUri: Uri?,
        inputCropRect: Rect,
    ): Result<GifticonImageCreateResult>

    suspend fun updateGifticonImage(
        inputCropUri: Uri,
        inputCropRect: Rect,
    ): Result<GifticonImageUpdateResult?>
}
