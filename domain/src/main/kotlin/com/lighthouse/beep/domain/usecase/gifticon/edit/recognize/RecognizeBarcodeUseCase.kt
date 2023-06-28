package com.lighthouse.beep.domain.usecase.gifticon.edit.recognize

import android.content.Context
import android.net.Uri
import com.lighthouse.beep.core.common.exts.decodeBitmap
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RecognizeBarcodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend operator fun invoke(uri: Uri): Result<String> {
        return runCatching {
            val bitmap = context.decodeBitmap(uri)
            BarcodeRecognizer().recognize(bitmap).barcode
        }
    }
}
