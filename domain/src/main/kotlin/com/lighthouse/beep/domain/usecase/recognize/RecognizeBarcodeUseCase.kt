package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import android.util.Log
import com.lighthouse.beep.core.common.exts.calculateSampleSize
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.core.common.exts.decodeSampledBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var totalTime = 0L
    private var count = 0

    private var totalStartTime = 0L
    private var totalEndTime = 0L

    suspend operator fun invoke(uri: Uri): Result<String> {
        return runCatching {
            if (count == 0) {
                totalStartTime = System.currentTimeMillis()
            }
            val startTime = System.currentTimeMillis()
            val sampleSize = context.calculateSampleSize(uri, 360)
            val bitmap = context.decodeSampledBitmap(uri, sampleSize)
            val barcode = BarcodeRecognizer().recognize(bitmap).barcode
            val endTime = System.currentTimeMillis()
            totalTime += endTime - startTime
            count += 1
            Log.d("Recognize", "[$count]total : ${totalTime} currentTime : ${endTime - startTime}, uri : $uri")
            if (count == 168) {
                totalEndTime = System.currentTimeMillis()
                Log.d("Recognize", "runningTime : ${totalEndTime - totalStartTime}")
            }


            barcode
        }
    }
}
