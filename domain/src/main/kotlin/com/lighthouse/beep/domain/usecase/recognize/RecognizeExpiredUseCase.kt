package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.ExpiredRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class RecognizeExpiredUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<Date> {
        return runCatching {
            val bitmap = context.decodeBitmap(uri)
            ExpiredRecognizer().recognize(bitmap).expired
        }
    }
}
