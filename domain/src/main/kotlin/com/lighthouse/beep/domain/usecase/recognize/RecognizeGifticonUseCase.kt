package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.GifticonRecognizer
import com.lighthouse.beep.library.recognizer.model.GifticonRecognizeInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RecognizeGifticonUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<GifticonRecognizeInfo> = withContext(Dispatchers.IO){
        runCatching {
            val bitmap = context.decodeBitmap(uri)
                ?: throw IOException("$uri decode Failed")
            GifticonRecognizer().recognize(bitmap)
        }
    }
}
