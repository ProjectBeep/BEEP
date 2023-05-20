package com.lighthouse.beep.domain.repository.gifticon

import android.net.Uri
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import java.util.Date

interface GifticonRecognizeRepository {

    suspend fun recognize(gallery: GalleryImage): Result<GifticonRecognizeResult>

    suspend fun recognizeText(uri: Uri): Result<String>

    suspend fun recognizeBarcode(uri: Uri): Result<String>

    suspend fun recognizeBalance(uri: Uri): Result<Int>

    suspend fun recognizeExpired(uri: Uri): Result<Date>
}
