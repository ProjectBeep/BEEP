package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.TextRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class RecognizeTextUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<String> {
        return runCatching {
            val bitmap = context.decodeBitmap(uri) ?: throw IOException("$uri decode Failed")
            val inputs = TextRecognizer().recognize(bitmap)
            inputs.joinToString("")
        }
    }
}