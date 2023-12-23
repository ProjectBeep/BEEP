package com.lighthouse.beep.data.local.repository.gifticon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.lighthouse.beep.core.common.exts.calculateCenterCropRect
import com.lighthouse.beep.core.common.exts.calculateSampleSize
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.common.exts.compressBitmap
import com.lighthouse.beep.core.common.exts.decodeSampledBitmap
import com.lighthouse.beep.core.common.exts.deleteFile
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.exists
import com.lighthouse.beep.core.common.exts.scale
import com.lighthouse.beep.core.common.utils.file.BeepDir
import com.lighthouse.beep.core.common.utils.file.BeepFileUtils
import com.lighthouse.beep.data.model.gifticon.GifticonOriginImageResult
import com.lighthouse.beep.data.model.gifticon.GifticonThumbnailImageResult
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalGifticonStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocalGifticonStorage {

    override suspend fun saveGifticonOriginImage(
        originUri: Uri,
    ): GifticonOriginImageResult = withContext(Dispatchers.IO) {
        val gifticonFile = BeepFileUtils.createFile(context, BeepDir.DIRECTORY_ORIGIN)
        val sampleSize = context.calculateSampleSize(originUri, displayWidth, displayHeight)
        val sampledGifticonBitmap = context.decodeSampledBitmap(originUri, sampleSize)
        gifticonFile.compressBitmap(sampledGifticonBitmap, Bitmap.CompressFormat.JPEG, 90)
        GifticonOriginImageResult(
            gifticonFile.toUri(),
            sampledGifticonBitmap,
        )
    }

    override suspend fun cropAndSaveGifticonThumbnailImage(
        gifticonImage: Bitmap,
    ): GifticonThumbnailImageResult = withContext(Dispatchers.IO) {
        val thumbnailFile = BeepFileUtils.createFile(context, BeepDir.DIRECTORY_THUMBNAIL)

        val cropRect = gifticonImage.calculateCenterCropRect(1f)
        val cropThumbnailBitmap = gifticonImage.crop(cropRect)
        thumbnailFile.compressBitmap(cropThumbnailBitmap, Bitmap.CompressFormat.JPEG, 90)

        GifticonThumbnailImageResult(
            thumbnailUri = thumbnailFile.toUri(),
            thumbnailRect = cropRect,
        )
    }

    override suspend fun saveGifticonThumbnailImage(
        thumbnailImage: Bitmap,
    ): Uri = withContext(Dispatchers.IO) {
        val thumbnailFile = BeepFileUtils.createFile(context, BeepDir.DIRECTORY_THUMBNAIL)
        thumbnailFile.compressBitmap(thumbnailImage, Bitmap.CompressFormat.JPEG, 90)
        thumbnailFile.toUri()
    }

    override suspend fun deleteFile(uri: Uri?) {
        context.deleteFile(uri)
    }
}
