package com.lighthouse.beep.data.repository.gifticon

import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import com.lighthouse.beep.data.model.gifticon.GifticonOriginImageResult
import com.lighthouse.beep.data.model.gifticon.GifticonThumbnailImageResult

interface LocalGifticonStorage {

    suspend fun saveGifticonOriginImage(
        originUri: Uri,
    ): GifticonOriginImageResult

    suspend fun cropAndSaveGifticonThumbnailImage(
        gifticonImage: Bitmap,
    ): GifticonThumbnailImageResult

    suspend fun saveGifticonThumbnailImage(
        thumbnailImage: Bitmap,
    ): Uri

    suspend fun deleteFile(uri: Uri?)
}
