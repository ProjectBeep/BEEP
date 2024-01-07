package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeScanMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val barcodeRecognizer =  BarcodeRecognizer()

    suspend operator fun invoke(uri: Uri): Result<String> = runCatching {
        val bitmap = context.decodeBitmap(uri) ?: throw IOException("이미지를 decode 하지 못했습니다")
        barcodeRecognizer.recognize(bitmap, BarcodeScanMode.IMAGE)
    }
}
