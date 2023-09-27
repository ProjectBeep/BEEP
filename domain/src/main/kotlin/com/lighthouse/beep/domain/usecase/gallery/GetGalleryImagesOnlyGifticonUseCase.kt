package com.lighthouse.beep.domain.usecase.gallery

import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGalleryImagesOnlyGifticonUseCase @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
) {

    suspend operator fun invoke(page: Int, limit: Int): List<GalleryImage> =
        withContext(Dispatchers.Default) {
            val images = galleryRepository.getImages(page, limit)
            val list = mutableListOf<GalleryImage>()
            images.filter {
                val recognize = galleryRepository.getRecognizeData(it)
                if (recognize == GalleryImageRecognizeData.GIFTICON) {
                    list.add(it)
                }
                recognize == GalleryImageRecognizeData.NONE
            }.map {
                launch {
                    val isGifticon =
                        recognizeBarcodeUseCase(it.contentUri).getOrDefault("").isNotEmpty()
                    galleryRepository.saveRecognizeData(it, isGifticon)
                    if (isGifticon) {
                        list.add(it)
                    }
                }
            }.joinAll()
            list.apply { sortBy { -it.dateAdded.time } }
        }
}