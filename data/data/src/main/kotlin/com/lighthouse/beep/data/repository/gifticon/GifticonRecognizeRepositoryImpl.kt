package com.lighthouse.beep.data.repository.gifticon

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.lighthouse.beep.core.exts.compressBitmap
import com.lighthouse.beep.core.exts.decodeBitmap
import com.lighthouse.beep.core.utils.file.BeepDir
import com.lighthouse.beep.core.utils.file.BeepFileUtils
import com.lighthouse.beep.data.mapper.toGifticonRecognizerResult
import com.lighthouse.beep.library.recognizer.BalanceRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.ExpiredRecognizer
import com.lighthouse.beep.library.recognizer.GifticonRecognizer
import com.lighthouse.beep.library.recognizer.TextRecognizer
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

internal class GifticonRecognizeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : GifticonRecognizeRepository {

    override suspend fun recognize(gallery: GalleryImage): Result<GifticonRecognizeResult> =
        runCatching {
            val bitmap = context.decodeBitmap(gallery.contentUri)
            val info = GifticonRecognizer().recognize(bitmap)
            var croppedUri: Uri? = null
            val croppedImage = info.croppedImage
            if (croppedImage != null) {
                val croppedFile =
                    BeepFileUtils.createFile(context, BeepDir.DIRECTORY_CROPPED).getOrThrow()
                croppedFile.compressBitmap(croppedImage, Bitmap.CompressFormat.JPEG, 100)
                croppedUri = croppedFile.toUri()
            }
            info.toGifticonRecognizerResult(gallery.contentUri, croppedUri)
        }

    override suspend fun recognizeText(uri: Uri): Result<String> = runCatching {
        val bitmap = context.decodeBitmap(uri)
        val inputs = TextRecognizer().recognize(bitmap)
        inputs.joinToString("")
    }

    override suspend fun recognizeBarcode(uri: Uri): Result<String> = runCatching {
        val bitmap = context.decodeBitmap(uri)
        BarcodeRecognizer().recognize(bitmap).barcode
    }

    override suspend fun recognizeBalance(uri: Uri): Result<Int> = runCatching {
        val bitmap = context.decodeBitmap(uri)
        BalanceRecognizer().recognize(bitmap).balance
    }

    override suspend fun recognizeExpired(uri: Uri): Result<Date> = runCatching {
        val bitmap = context.decodeBitmap(uri)
        ExpiredRecognizer().recognize(bitmap).expired
    }
}
