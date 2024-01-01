package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.core.common.exts.add
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.common.exts.remove
import com.lighthouse.beep.core.common.exts.removeAt
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.core.ui.recyclerview.GridCalculator
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryRecognize
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import com.lighthouse.beep.ui.feature.gallery.model.DragMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        private const val unitCount = 5

        val spanCount = GridCalculator.getSpanCount(117.dp, 4.dp, 0, 3)
        private val rawCount = GridCalculator.getRowCount(117.dp, 4.dp)
        private val pageCount = spanCount * rawCount

        const val maxSelectCount = 30
    }

    private val _bucketType = MutableStateFlow(BucketType.RECOMMEND)
    val bucketType = _bucketType.asStateFlow()

    fun setBucketType(bucketType: BucketType) {
        _bucketType.value = bucketType
    }

    private val bucketScrollInfoMap = mutableMapOf<BucketType, ScrollInfo>()

    val bucketScroll
        get() = bucketScrollInfoMap.getOrDefault(bucketType.value, ScrollInfo.None)

    fun setBucketScroll(
        type: BucketType = bucketType.value,
        scrollInfo: ScrollInfo,
    ) {
        bucketScrollInfoMap[type] = scrollInfo
    }

    private val recognizeData = mutableMapOf<String, GalleryRecognize>()
    private var loadedRecognizeData = false

    private val _recommendList = MutableStateFlow<List<GalleryImage>>(emptyList())
    val recommendList = _recommendList.asStateFlow()

    private val recommendIdSet = mutableSetOf<Long>()
    private var pageOffset = 0

    private var currentPage = 0
    private val maxPage = ceil(galleryRepository.getImageSize() / pageCount.toFloat()).toInt()

    private var firstVisible = 0

    private val loadingRecognizeData = MutableStateFlow(false)
    private val recognizing = MutableStateFlow(false)

    val showRecognizeProgress = combine(
        bucketType,
        recognizing,
        loadingRecognizeData,
    ) { type, recognizing, loadingRecognizeData ->
        type == BucketType.RECOMMEND && (recognizing || loadingRecognizeData)
    }

    private var requestNextJob: Job? = null

    fun requestRecommend(currentFirstVisible: Int = firstVisible) {
        firstVisible = currentFirstVisible

        if (requestNextJob?.isActive == true || currentPage >= maxPage) {
            return
        }
        if (loadedRecognizeData) {
            requestRecommendNext()
        } else {
            viewModelScope.launch {
                loadingRecognizeData.value = true
                recognizeData +=
                    galleryRepository.getRecognizeDataList().associateBy { it.imagePath }
                loadedRecognizeData = true
                requestRecommendNext()
            }.invokeOnCompletion {
                loadingRecognizeData.value = false
            }
        }
    }

    private fun requestRecommendNext() {
        requestNextJob = viewModelScope.launch {
            recognizing.value = true
            val targetSize = recommendList.value.size + (pageCount * 1.5f).toInt()
            val list = mutableListOf<GalleryImage>()
            while (currentPage < maxPage && recommendList.value.size < targetSize) {
                val images = galleryRepository.getImages(currentPage, pageCount, pageOffset)
                val requestRecognizeList = images.filter {
                    if (it.id in recommendIdSet) {
                        return@filter false
                    }

                    val data = recognizeData[it.imagePath]
                    if (data?.dateAdded == it.dateAdded && data.isGifticon) {
                        list.add(it)
                    }
                    data == null
                }

                requestRecognizeList.windowed(unitCount, unitCount).forEach { requestList ->
                    requestList.map {
                        launch(Dispatchers.IO) {
                            val isGifticon =
                                recognizeBarcodeUseCase(it.contentUri).getOrDefault("").isNotEmpty()
                            galleryRepository.saveRecognizeData(it, isGifticon)
                            if (isGifticon) {
                                list.add(it)
                            }
                        }
                    }.joinAll()

                    if (recommendList.value.size - 1 <= firstVisible + pageCount) {
                        appendRecommendList(list)
                    }
                }
                currentPage += 1

                if (recommendList.value.size - 1 <= firstVisible + pageCount) {
                    appendRecommendList(list)
                }
            }
            appendRecommendList(list)
        }.also {
            it.invokeOnCompletion {
                recognizing.value = false
            }
        }
    }

    private fun appendRecommendList(list: MutableList<GalleryImage>) {
        if (list.isEmpty()) {
            return
        }
        recommendIdSet.addAll(list.map { it.id })

        list.sortBy { -it.dateAdded.time }
        _recommendList.value += list
        list.clear()
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

    fun isSelectedItem(item: GalleryImage): Boolean {
        return _selectedList.value.indexOfFirst { it.id == item.id } != -1
    }

    fun dragItem(item: GalleryImage, dragMode: DragMode) {
        when (dragMode) {
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
        if (id in recommendIdSet) {
            return
        }

        viewModelScope.launch {
            val item = galleryRepository.getImage(id) ?: return@launch

            pageOffset += 1

            recommendIdSet.add(item.id)
            val isGifticon =
                recognizeBarcodeUseCase(item.contentUri).getOrDefault("").isNotEmpty()
            galleryRepository.saveRecognizeData(item, isGifticon)
            if (isGifticon) {
                _recommendList.value = listOf(item) + _recommendList.value
            }
        }
    }

    fun deleteGalleryContent(id: Long) {
        if (id in recommendIdSet) {
            pageOffset -= 1

            _selectedList.remove { it.id == id }
            _recommendList.remove { it.id == id }
            recommendIdSet.remove(id)
        }
    }
}