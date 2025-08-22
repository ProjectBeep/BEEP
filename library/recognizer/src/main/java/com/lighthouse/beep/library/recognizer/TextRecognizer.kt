package com.lighthouse.beep.library.recognizer

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class TextRecognizer {

    private val options = KoreanTextRecognizerOptions.Builder().build()

    private val client = TextRecognition.getClient(options)

    suspend fun recognize(bitmap: Bitmap): List<String> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            client.process(image).addOnCompleteListener {
                val texts = when {
                    it.isSuccessful -> it.result.text.lines().filter { line -> line != "" }
                    else -> emptyList()
                }
                continuation.resume(texts)
            }
        }
    }
}
