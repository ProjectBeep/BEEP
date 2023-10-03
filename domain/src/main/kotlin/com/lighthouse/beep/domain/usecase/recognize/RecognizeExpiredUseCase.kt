package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.ExpiredRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class RecognizeExpiredUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<Date> = withContext(Dispatchers.Default){
        runCatching {
            val bitmap = context.decodeBitmap(uri) ?: throw IOException("$uri decode Failed")
            ExpiredRecognizer().recognize(bitmap).expired
        }
    }

    suspend operator fun invoke(bitmap: Bitmap): Result<Date> = withContext(Dispatchers.Default){
        runCatching {
            ExpiredRecognizer().recognize(bitmap).expired
        }
    }
}
