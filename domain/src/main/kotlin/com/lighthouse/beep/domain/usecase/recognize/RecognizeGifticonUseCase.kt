package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.domain.mapper.toGifticonRecognizerResult
import com.lighthouse.beep.library.recognizer.GifticonRecognizer
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RecognizeGifticonUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(gallery: GalleryImage): Result<GifticonRecognizeResult> = withContext(Dispatchers.Default){
        runCatching {
            val bitmap = context.decodeBitmap(gallery.contentUri)
                ?: throw IOException("${gallery.contentUri} decode Failed")
            val info = GifticonRecognizer().recognize(bitmap)
            info.toGifticonRecognizerResult(gallery.contentUri)
        }
    }
}
