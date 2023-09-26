package com.lighthouse.beep.domain.usecase.gallery

import android.util.Log
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGalleryImagesOnlyGifticonUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
) {

    suspend operator fun invoke(page: Int, limit: Int): List<GalleryImage> = withContext(Dispatchers.Default){
        val images = galleryRepository.getImages(page, limit)
        val list = mutableListOf<GalleryImage>()
        images.map {
            Log.d("Recognize", "recognize : ${it.id}")
            launch {

                val barcode = recognizeBarcodeUseCase(it.contentUri).getOrDefault("")
                if (barcode.isNotEmpty()) {
                    list.add(it)
                }
            }
        }.joinAll()
        list.apply {
            sortBy { it.date }
        }
    }
}