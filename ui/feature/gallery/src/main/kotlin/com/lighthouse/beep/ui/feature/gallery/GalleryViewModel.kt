package com.lighthouse.beep.ui.feature.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.filter
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.dp
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getGalleryImagesOnlyGifticonUseCase: GetGalleryImagesOnlyGifticonUseCase,
) : ViewModel() {

    private val _bucketType = MutableStateFlow(BucketType.RECOMMEND)
    val bucketType = _bucketType.asStateFlow()

    val spanCount = calculateSpanCount()
    val pageSize: Int = calculatePageSize()
    val pageFetchCount: Int = calculatePageFetchCount()

    private fun calculateSpanCount(): Int {
        val imageWidth = 117.dp
        val space = 4.dp
        return displayWidth / (imageWidth + space)
    }

    private fun calculatePageSize(): Int {
        val imageHeight = 117.dp
        val space = 4.dp
        val rowCount = displayHeight / (imageHeight + space)

        return (spanCount * rowCount * 1.5f).toInt()
    }

    private fun calculatePageFetchCount(): Int {
        val imageHeight = 117.dp
        val space = 4.dp
        val rowCount = displayHeight / (imageHeight + space)

        return (spanCount * rowCount * 0.5f).toInt()
    }

//    val galleryImages = getGalleryImagesUseCase(pageSize).map { data ->
//        data.filter {
//            recognizeBarcodeUseCase(it.contentUri).getOrDefault("").isNotEmpty()
//        }
//    }

    private val _appendGalleryImages = MutableSharedFlow<List<GalleryImage>>(extraBufferCapacity=5)
    val appendGalleryImages = _appendGalleryImages.asSharedFlow()

    private var currentPage = 0

    private val list = mutableListOf<GalleryImage>()

    private var job: Job? = null

    fun requestNext(targetSize: Int) {
        if (job?.isActive == true){
            return
        }
        job = viewModelScope.launch {
            while (list.size < targetSize) {
                Log.d("Recognize", "currentPage : ${currentPage}, size : ${list.size}, target : ${targetSize}")
                val items = getGalleryImagesOnlyGifticonUseCase(currentPage++, 20)
                list.addAll(items)
                _appendGalleryImages.emit(items)
            }
        }
    }
}