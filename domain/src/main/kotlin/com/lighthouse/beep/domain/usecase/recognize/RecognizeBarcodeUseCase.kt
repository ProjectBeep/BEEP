package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.calculateSampleSize
import com.lighthouse.beep.core.common.exts.decodeSampledBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeScanMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val barcodeRecognizer =  BarcodeRecognizer()

    suspend operator fun invoke(uri: Uri): Result<String> = withContext(Dispatchers.Default){
        runCatching {
            val sampleSize = context.calculateSampleSize(uri, 360)
            val bitmap = context.decodeSampledBitmap(uri, sampleSize)
            barcodeRecognizer.recognize(bitmap, BarcodeScanMode.IMAGE)
        }
    }
}
