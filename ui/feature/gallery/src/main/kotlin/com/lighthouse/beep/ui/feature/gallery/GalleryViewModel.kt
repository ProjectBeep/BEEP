package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImageSizeUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesOnlyGifticonUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    getGalleryImageSizeUseCase: GetGalleryImageSizeUseCase,
    getGalleryImagesUseCase: GetGalleryImagesUseCase,
    private val getGalleryImagesOnlyGifticonUseCase: GetGalleryImagesOnlyGifticonUseCase,
) : ViewModel() {

    companion object {
        private val imageWidth = 117.dp
        private val imageHeight = 117.dp
        private val space = 4.dp
        private const val limit = 5

        val spanCount = displayWidth / (imageWidth + space)
        val pageCount: Int = spanCount * displayHeight / (imageHeight + space)
        val pageFetchCount: Int = pageCount / 2
    }

    private val _bucketType = MutableStateFlow(BucketType.RECOMMEND)
    val bucketType = _bucketType.asStateFlow()

    fun setBucketType(bucketType: BucketType) {
        _bucketType.value = bucketType
    }

    private val _recommendList = MutableStateFlow<List<GalleryImage>>(emptyList())
    val recommendList = _recommendList.asStateFlow()

    private var currentPage = 0
    private val maxPage = ceil(getGalleryImageSizeUseCase() / limit.toFloat()).toInt()

    private var lastVisible = 0

    private val recognizing = MutableStateFlow(false)

    val showRecognizeProgress = combine(
        bucketType,
        recognizing
    ) { type, recognizing ->
        type == BucketType.RECOMMEND && recognizing
    }

    private var requestNextJob: Job? = null

    fun requestRecommendNext(lastVisiblePosition: Int = lastVisible) {
        if (
            requestNextJob?.isActive == true ||
            currentPage >= maxPage ||
            lastVisiblePosition < recommendList.value.size - pageFetchCount) {
            return
        }
        lastVisible = lastVisiblePosition
        requestNextJob = viewModelScope.launch {
            launch {
                delay(100)
                recognizing.value = true
            }
            val targetSize = recommendList.value.size + pageCount
            while (currentPage < maxPage && recommendList.value.size < targetSize) {
                val items = getGalleryImagesOnlyGifticonUseCase(currentPage, limit)
                _recommendList.value += items
                currentPage += 1
            }
        }.also {
            it.invokeOnCompletion {
                recognizing.value = false
            }
        }
    }

    fun cancelRecommendNext() {
        requestNextJob?.cancel()
    }

    val allList = getGalleryImagesUseCase(pageCount)
}