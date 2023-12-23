package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.core.common.exts.add
import com.lighthouse.beep.core.common.exts.displayHeight
import com.lighthouse.beep.core.common.exts.displayWidth
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.common.exts.remove
import com.lighthouse.beep.core.common.exts.removeAt
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import com.lighthouse.beep.ui.feature.gallery.model.DragMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
) : ViewModel() {

    companion object {
        private val imageWidth = 117.dp
        private val imageHeight = 117.dp
        private val space = 4.dp
        private const val limit = 5

        val spanCount = displayWidth / (imageWidth + space)
        val pageCount: Int = spanCount * displayHeight / (imageHeight + space)
        val pageFetchCount: Int = pageCount / 2

        const val maxSelectCount = 30
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

    private val recommendSearchIdSet = mutableSetOf<Long>()
    private var pageOffset = 0

    private var currentPage = 0
    private val maxPage = ceil(galleryRepository.getImageSize() / limit.toFloat()).toInt()

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
            recognizing.value = true
            val targetSize = recommendList.value.size + pageCount
            while (currentPage < maxPage && recommendList.value.size < targetSize) {
                val images = galleryRepository.getImages(currentPage, limit, pageOffset)
                val list = mutableListOf<GalleryImage>()
                val requestRecognizeList = images.filter {
                    recommendSearchIdSet.add(it.id)
                    val recognize = galleryRepository.getRecognizeData(it)
                    if (recognize == GalleryImageRecognizeData.GIFTICON) {
                        list.add(it)
                    }
                    recognize == GalleryImageRecognizeData.NONE
                }

                if (requestRecognizeList.isNotEmpty()) {
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
                    list.sortBy { -it.dateAdded.time }
                }
                _recommendList.value += list
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

    val allList = galleryRepository.getImages(pageCount)

    private val _selectedList = MutableStateFlow<List<GalleryImage>>(emptyList())
    val selectedList = _selectedList.asStateFlow()

    val isSelected = selectedList.map {
        it.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val isSelectable
        get() = selectedList.value.size < maxSelectCount

    fun toggleItem(item: GalleryImage) {
        if (!isSelectable) {
            return
        }

        val index = _selectedList.value.indexOfFirst { it.id == item.id }
        if (index == -1) {
            _selectedList.add(item)
        } else {
            _selectedList.removeAt(index)
        }
    }

    fun isSelectedItem(item: GalleryImage) : Boolean {
        return _selectedList.value.indexOfFirst { it.id == item.id } != -1
    }

    fun dragItem(item: GalleryImage, dragMode: DragMode) {
        when(dragMode) {
            DragMode.SELECT -> selectItem(item)
            DragMode.DELETE -> deleteItem(item)
            else -> {}
        }
    }

    private fun selectItem(item: GalleryImage) {
        if (!isSelectable) {
            return
        }
        if (_selectedList.value.find { it.id == item.id } != null) {
            return
        }
        _selectedList.add(item)
    }

    fun deleteItem(item: GalleryImage) {
        _selectedList.remove { it.id == item.id }
    }

    fun setItems(list: List<GalleryImage>) {
        _selectedList.value = list
    }

    fun insertGalleryContent(id: Long) {
        viewModelScope.launch {
            val item = galleryRepository.getImage(id)  ?: return@launch

            pageOffset += 1

            recommendSearchIdSet.add(item.id)
            val isGifticon =
                recognizeBarcodeUseCase(item.contentUri).getOrDefault("").isNotEmpty()
            galleryRepository.saveRecognizeData(item, isGifticon)
            if (isGifticon) {
                _recommendList.value = listOf(item) + _recommendList.value
            }
        }
    }

    fun deleteGalleryContent(id: Long) {
        if (id in recommendSearchIdSet) {
            pageOffset -= 1

            _selectedList.remove { it.id == id }
            _recommendList.remove { it.id == id }
        }
    }
}