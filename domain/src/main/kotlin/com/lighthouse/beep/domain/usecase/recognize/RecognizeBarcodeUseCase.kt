package com.lighthouse.beep.domain.usecase.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeScanMode
import com.lighthouse.beep.library.recognizer.model.BarcodeInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val barcodeRecognizer =  BarcodeRecognizer()

    suspend operator fun invoke(uri: Uri): Result<BarcodeInfo> = runCatching {
        val bitmap = context.decodeBitmap(uri) ?: throw IOException("이미지를 decode 하지 못했습니다")
        if (bitmap.width > 32 && bitmap.height > 32) {
            barcodeRecognizer.recognize(bitmap, BarcodeScanMode.IMAGE)
        } else{
            BarcodeInfo.None
        }
    }
}
