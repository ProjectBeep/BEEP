package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.lighthouse.beep.core.common.exts.compressBitmap
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.core.common.utils.file.BeepDir
import com.lighthouse.beep.core.common.utils.file.BeepFileUtils
import com.lighthouse.beep.domain.mapper.toGifticonRecognizerResult
import com.lighthouse.beep.library.recognizer.GifticonRecognizer
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class RecognizeGifticonUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(gallery: GalleryImage): Result<GifticonRecognizeResult> {
        return runCatching {
            val bitmap = context.decodeBitmap(gallery.contentUri)
                ?: throw IOException("${gallery.contentUri} decode Failed")
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
    }
}
