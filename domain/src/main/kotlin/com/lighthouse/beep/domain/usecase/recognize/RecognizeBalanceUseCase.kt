package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.BalanceRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RecognizeBalanceUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<Int> = withContext(Dispatchers.Default){
        runCatching {
            val bitmap = context.decodeBitmap(uri) ?: throw IOException("$uri decode Failed")
            BalanceRecognizer().recognize(bitmap).balance
        }
    }

    suspend operator fun invoke(bitmap: Bitmap): Result<Int> = withContext(Dispatchers.Default){
        runCatching {
            BalanceRecognizer().recognize(bitmap).balance
        }
    }
}
