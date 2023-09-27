package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImageSizeUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesOnlyGifticonUseCase
import com.lighthouse.beep.domain.usecase.gallery.GetGalleryImagesUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val bucketScrollInfoMap = mutableMapOf<BucketType, ScrollInfo>()

    val bucketScroll
        get() = bucketScrollInfoMap.getOrDefault(bucketType.value, ScrollInfo.None)

    fun setBucketScroll(
        type: BucketType = bucketType.value,
        scrollInfo: ScrollInfo,
    ) {
        bucketScrollInfoMap[type] = scrollInfo
    }

    fun setBucketType(bucketType: BucketType) {
        _bucketType.value = bucketType
    }

    private val _recommendList = MutableStateFlow<List<GalleryImage>>(emptyList())
    val recommendList = _recommendList.asStateFlow()

    private var currentPage = 0
    private val maxPage = ceil(getGalleryImageSizeUseCase() / limit.toFloat()).toInt()

    private var lastVisible = 0

    private val recognizing = MutableStateFlow(false)

    private val needRecommendFetch = MutableStateFlow(false)

    val showRecognizeProgress = combine(
        bucketType,
        needRecommendFetch,
        recognizing,
    ) { type, needRecommendFetch, recognizing ->
        type == BucketType.RECOMMEND && needRecommendFetch && recognizing
    }

    private var requestNextJob: Job? = null

    fun requestRecommendNext(currentLastVisible: Int? = null) {
        needRecommendFetch.value = if (currentLastVisible == null) {
            lastVisible >= recommendList.value.size - pageFetchCount
        } else {
            currentLastVisible >= recommendList.value.size - pageFetchCount
        }

        if (requestNextJob?.isActive == true ||
            currentPage >= maxPage ||
            !needRecommendFetch.value
        ) {
            return
        }

        lastVisible = currentLastVisible ?: lastVisible
        requestNextJob = viewModelScope.launch {
            launch {
                delay(200)
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

    private val _selectedList = mutableListOf<GalleryImage>()
    val selectedList
        get() = _selectedList.toList()

    private val _selectedListFlow = MutableSharedFlow<List<GalleryImage>>(replay = 1)
    val selectedListFlow = _selectedListFlow.asSharedFlow()

    val isSelected = selectedListFlow.map {
        it.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun selectItem(item: GalleryImage) {
        val index = _selectedList.indexOfFirst { it.id == item.id }
        if (index == -1){
            _selectedList.add(item)
        } else {
            _selectedList.removeAt(index)
        }
        viewModelScope.launch {
            _selectedListFlow.emit(_selectedList)
        }
    }

    fun deleteItem(item:GalleryImage) {
        val index = _selectedList.indexOfFirst { it.id == item.id }
        if (index == -1){
            return
        } else {
            _selectedList.removeAt(index)
        }
        viewModelScope.launch {
            _selectedListFlow.emit(_selectedList)
        }
    }

    fun setItems(list: List<GalleryImage>) {
        _selectedList.clear()
        _selectedList.addAll(list)
        viewModelScope.launch {
            _selectedListFlow.emit(_selectedList)
        }
    }

    init {
        viewModelScope.launch {
            _selectedListFlow.emit(emptyList())
        }
    }
}