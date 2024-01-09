package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.common.exts.add
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.common.exts.remove
import com.lighthouse.beep.core.common.exts.removeAt
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.core.ui.recyclerview.GridCalculator
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageData
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import com.lighthouse.beep.ui.feature.gallery.model.DragMode
import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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
    private val gifticonRepository: GifticonRepository,
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

    private val recognizeData = mutableMapOf<String, GalleryImageData>()

    private val gifticonData = mutableSetOf<String>()
    private val _gifticonDataFlow = MutableSharedFlow<Set<String>>(replay = 1)
    val gifticonDataFlow = _gifticonDataFlow.asSharedFlow()

    private var loadedRecognizeData = false

    private val _useSelectedStorage = MutableStateFlow(false)
    val useSelectedStorage = _useSelectedStorage.asStateFlow()

    fun setUseSelectedStorage(value: Boolean) {
        _useSelectedStorage.value = value
    }

    private val recommendImageList = MutableStateFlow<List<GalleryImage>>(emptyList())

    val recommendList = combine(
        useSelectedStorage,
        recommendImageList
    ) { useSelectedStorage, list ->
        if (useSelectedStorage) {
            listOf(GalleryItem.AddItem) + list.map { GalleryItem.Image(it) }
        } else {
            list.map { GalleryItem.Image(it) }
        }
    }

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

                gifticonRepository.getGifticonImageDataList(BeepAuth.userUid).forEach {
                    gifticonData += "${it.imagePath}-${it.imageAddedDate.time}"
                }
                _gifticonDataFlow.emit(gifticonData)

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
            val targetSize = recommendImageList.value.size + (pageCount * 1.5f).toInt()
            val list = mutableListOf<GalleryImage>()
            while (currentPage < maxPage && recommendImageList.value.size < targetSize) {
                val images = galleryRepository.getImages(currentPage, pageCount, pageOffset)
                val requestRecognizeList = images.filter {
                    if (it.id in recommendIdSet) {
                        return@filter false
                    }

                    val data = recognizeData[it.imagePath]
                    if (data?.addedDate == it.dateAdded && data.isGifticon) {
                        list.add(it)
                    }
                    data == null
                }

                requestRecognizeList.windowed(unitCount, unitCount).forEach { requestList ->
                    requestList.map {
                        launch(Dispatchers.IO) {
                            val barcode = recognizeBarcodeUseCase(it.contentUri).getOrNull()
                            val isGifticon = !barcode?.barcode.isNullOrEmpty()
                            galleryRepository.saveRecognizeData(it, isGifticon)
                            if (isGifticon) {
                                list.add(it)
                            }
                        }
                    }.joinAll()

                    if (recommendImageList.value.size - 1 <= firstVisible + pageCount) {
                        appendRecommendList(list)
                    }
                }
                currentPage += 1

                if (recommendImageList.value.size - 1 <= firstVisible + pageCount) {
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
        recommendImageList.value += list
        list.clear()
    }

    fun cancelRecommendNext() {
        requestNextJob?.cancel()
    }

    val allList = galleryRepository.getImages(pageCount).map { data ->
        data.map {
            if (it.id == -1L) {
                GalleryItem.AddItem
            } else {
                GalleryItem.Image(it)
            }
        }
    }

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

    fun isSelectedItem(item: GalleryItem.Image): Boolean {
        return _selectedList.value.indexOfFirst { it.id == item.item.id } != -1
    }

    fun dragItem(item: GalleryItem.Image, dragMode: DragMode) {
        when (dragMode) {
            DragMode.SELECT -> selectItem(item.item)
            DragMode.DELETE -> deleteItem(item.item)
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
            val barcode = recognizeBarcodeUseCase(item.contentUri).getOrNull()
            val isGifticon = !barcode?.barcode.isNullOrEmpty()
            galleryRepository.saveRecognizeData(item, isGifticon)
            if (isGifticon) {
                recommendImageList.value = listOf(item) + recommendImageList.value
            }
        }
    }

    fun deleteGalleryContent(id: Long) {
        if (id in recommendIdSet) {
            pageOffset -= 1

            _selectedList.remove { it.id == id }
            recommendImageList.remove { it.id == id }
            recommendIdSet.remove(id)
        }
    }

    init {
        viewModelScope.launch {
            _gifticonDataFlow.emit(emptySet())
        }
    }
}