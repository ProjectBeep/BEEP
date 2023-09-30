package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGalleryGifticonUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase
){

    suspend operator fun invoke(requestRecognizeList: List<GalleryImage>) = withContext(Dispatchers.Default) {
        val list = mutableListOf<GalleryImage>()
        requestRecognizeList.map {
            launch {
                val isGifticon =
                    recognizeBarcodeUseCase(it.contentUri).getOrDefault("").isNotEmpty()
                galleryRepository.saveRecognizeData(it, isGifticon)
                if (isGifticon) {
                    list.add(it)
                }
            }
        }.joinAll()
        list
    }
}