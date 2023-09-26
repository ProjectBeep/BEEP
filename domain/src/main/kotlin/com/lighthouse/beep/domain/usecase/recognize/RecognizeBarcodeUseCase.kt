package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.calculateSampleSize
import com.lighthouse.beep.core.common.exts.decodeSampledBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return runCatching {
            val sampleSize = context.calculateSampleSize(uri, 360)
            val bitmap = context.decodeSampledBitmap(uri, sampleSize)
            BarcodeRecognizer().recognize(bitmap).barcode
        }
    }
}
