package com.lighthouse.beep.content.repository.gifticon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.lighthouse.beep.core.exts.calculateCenterCropRect
import com.lighthouse.beep.core.exts.calculateSampleSize
import com.lighthouse.beep.core.exts.centerCrop
import com.lighthouse.beep.core.exts.compressBitmap
import com.lighthouse.beep.core.exts.decodeSampledBitmap
import com.lighthouse.beep.core.exts.exists
import com.lighthouse.beep.core.exts.scale
import com.lighthouse.beep.core.utils.file.BeepDir
import com.lighthouse.beep.core.utils.file.BeepFileUtils
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonStorage
import com.lighthouse.beep.model.gifticon.GifticonImageCreateResult
import com.lighthouse.beep.model.gifticon.GifticonImageUpdateResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalGifticonStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocalGifticonStorage {

    override suspend fun createGifticonImage(
        inputOriginUri: Uri,
        inputCropUri: Uri?,
        inputCropRect: Rect,
    ): Result<GifticonImageCreateResult> = runCatching {
        withContext(Dispatchers.IO) {
            val outputOriginFile =
                BeepFileUtils.createFile(context, BeepDir.DIRECTORY_ORIGIN).getOrThrow()
            val sampleSize = context.calculateSampleSize(inputOriginUri)
            val sampledOriginBitmap = context.decodeSampledBitmap(inputOriginUri, sampleSize)
            outputOriginFile.compressBitmap(sampledOriginBitmap, Bitmap.CompressFormat.JPEG, 100)

            val outputCroppedFile =
                BeepFileUtils.createFile(context, BeepDir.DIRECTORY_CROPPED).getOrThrow()
            val cropped: Bitmap
            val croppedRect: Rect
            if (inputCropUri?.exists(context) == true) {
                cropped = context.decodeSampledBitmap(inputCropUri, sampleSize)
                croppedRect = inputCropRect.scale(1f / sampleSize)
            } else {
                croppedRect = sampledOriginBitmap.calculateCenterCropRect(1f)
                cropped = sampledOriginBitmap.centerCrop(croppedRect)
            }

            outputCroppedFile.compressBitmap(cropped, Bitmap.CompressFormat.JPEG, 100)
            GifticonImageCreateResult(
                outputOriginFile.toUri(),
                outputCroppedFile.toUri(),
                croppedRect,
            )
        }
    }

    override suspend fun updateGifticonImage(
        inputCropUri: Uri,
        inputCropRect: Rect,
    ): Result<GifticonImageUpdateResult?> = runCatching {
        if (!inputCropUri.exists(context)) {
            return@runCatching null
        }

        withContext(Dispatchers.IO) {
            val outputCroppedFile =
                BeepFileUtils.createFile(context, BeepDir.DIRECTORY_CROPPED).getOrThrow()
            inputCropUri.toFile().copyTo(outputCroppedFile)
            GifticonImageUpdateResult(
                outputCroppedFile.toUri(),
                inputCropRect,
            )
        }
    }
}
