package com.lighthouse.beep.ui.feature.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.filter
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImageSizeUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesOnlyGifticonUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getGalleryImageSizeUseCase: GetGalleryImageSizeUseCase,
    private val getGalleryImagesOnlyGifticonUseCase: GetGalleryImagesOnlyGifticonUseCase,
    private val getGalleryImagesUseCase: GetGalleryImagesUseCase,
) : ViewModel() {

    companion object {
        private val imageWidth = 117.dp
        private val imageHeight = 117.dp
        private val space = 4.dp
        private const val limit = 20

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

    private val recognizing = MutableStateFlow(false)

    val showRecognizeProgress = combine(
        bucketType,
        recognizing
    ) { type, recognizing ->
        type == BucketType.RECOMMEND && recognizing
    }

    fun requestNext(lastVisiblePosition: Int = 0) {
        if (
            recognizing.value ||
            currentPage >= maxPage ||
            lastVisiblePosition < recommendList.value.size - pageFetchCount) {
            return
        }
        recognizing.value = true
        val targetSize = recommendList.value.size + pageCount
        viewModelScope.launch {
            while (currentPage < maxPage && recommendList.value.size < targetSize) {
                val items = getGalleryImagesOnlyGifticonUseCase(currentPage, limit)
                _recommendList.value += items
                currentPage += 1
            }
            recognizing.value = false
        }
    }

    val allList = getGalleryImagesUseCase(pageCount)
}