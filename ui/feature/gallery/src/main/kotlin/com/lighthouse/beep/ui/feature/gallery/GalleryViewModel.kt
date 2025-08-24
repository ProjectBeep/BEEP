package com.lighthouse.beep.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.lighthouse.beep.core.common.exts.add
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.common.exts.remove
import com.lighthouse.beep.core.common.exts.removeAt
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.core.ui.recyclerview.GridCalculator
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.model.DragMode
import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryImageRepository,
    private val gifticonRepository: GifticonRepository,
) : ViewModel() {

    companion object {
        val spanCount = GridCalculator.getSpanCount(117.dp, 4.dp, 0, 3)
        private val rawCount = GridCalculator.getRowCount(117.dp, 4.dp)
        private val pageCount = spanCount * rawCount

        const val maxSelectCount = 30
    }

    private var scrollInfo = ScrollInfo.None

    fun setScrollInfo(scrollInfo: ScrollInfo) {
        this.scrollInfo = scrollInfo
    }

    fun getScrollInfo(): ScrollInfo = scrollInfo

    private val gifticonData = mutableSetOf<String>()
    private val _gifticonDataFlow = MutableSharedFlow<Set<String>>(replay = 1)
    val gifticonDataFlow = _gifticonDataFlow.asSharedFlow()

    private val useSelectedStorage = MutableStateFlow(false)
    val useSelectedStorageValue
        get() = useSelectedStorage.value

    fun setUseSelectedStorage(value: Boolean) {
        useSelectedStorage.value = value
    }

    val allList = useSelectedStorage.flatMapLatest { useSelectedStorage ->
        if (useSelectedStorage) {
            galleryRepository.getImages(pageCount).map { data ->
                data.map<GalleryImage, GalleryItem> { GalleryItem.Image(it) }
                    .insertHeaderItem(TerminalSeparatorType.FULLY_COMPLETE, GalleryItem.AddItem)
            }.cachedIn(viewModelScope)
        } else {
            galleryRepository.getImages(pageCount).map { data ->
                data.map<GalleryImage, GalleryItem> { GalleryItem.Image(it) }
            }.cachedIn(viewModelScope)
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

    fun refreshGalleryImage() {
        // 갤러리 이미지 새로고침 (필요시 추가 로직)
    }

    fun insertGalleryContent(id: Long) {
        // 갤러리 컨텐츠 추가 시 처리 (필요시 추가 로직)
    }

    fun deleteGalleryContent(id: Long) {
        // 갤러리 컨텐츠 삭제 시 처리
        _selectedList.remove { it.id == id }
    }

    init {
        viewModelScope.launch {
            gifticonRepository.getGifticonImageDataList(com.lighthouse.beep.auth.BeepAuth.userUid).forEach {
                gifticonData += "${it.imagePath}-${it.imageAddedDate.time}"
            }
            _gifticonDataFlow.emit(gifticonData)
        }
    }
}