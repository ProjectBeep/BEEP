package com.lighthouse.beep.ui.feature.editor

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.recognize.RecognizeGifticonUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import com.lighthouse.beep.ui.feature.editor.model.toGifticonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recognizeGifticonUseCase: RecognizeGifticonUseCase,
) : ViewModel() {

    private val _galleryImage = MutableStateFlow(EditorParam.getGalleryList(savedStateHandle))
    val galleryImage = _galleryImage.asStateFlow()

    fun deleteItem(item: GalleryImage) {
        val oldList = _galleryImage.value
        val index = oldList.indexOfFirst { it.id == item.id }
        val newSelectItem = when {
            index + 1 < oldList.size -> oldList[index + 1]
            index - 1 >= 0 -> oldList[index - 1]
            else -> null
        }
        if (newSelectItem != null) {
            selectGifticon(newSelectItem)
        }

        _galleryImage.value = oldList.subList(0, index) + oldList.subList(index + 1, oldList.size)
        gifticonDataMap.remove(item.id)
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }
    }

    fun deleteGalleryContent(id: Long) {
        val item = _galleryImage.value.find { it.id == id } ?: return
        deleteItem(item)
    }

    private val _selectedGifticon = MutableStateFlow(_galleryImage.value.firstOrNull())
    val selectedGifticon = _selectedGifticon.asStateFlow()

    fun selectGifticon(item: GalleryImage) {
        _selectedGifticon.value = item
    }

    fun selectGifticon(position: Int) {
        val item = galleryImage.value.getOrNull(position) ?: return
        selectGifticon(item)
    }

    private val _selectedEditorChip = MutableStateFlow<EditorChip>(EditorChip.Preview)
    val selectedEditorChip = _selectedEditorChip.asStateFlow()

    val selectedEditType: EditType?
        get() {
            val chip = selectedEditorChip.value as? EditorChip.Property
            return chip?.type
        }

    val selectedEditorPage = selectedEditorChip.map { chip ->
        when (chip) {
            is EditorChip.Preview -> EditorPage.PREVIEW
            else -> EditorPage.CROP
        }
    }.distinctUntilChanged()

    fun selectEditorChip(item: EditorChip) {
        _selectedEditorChip.value = item
    }

    private val gifticonDataMap = mutableMapOf<Long, GifticonData>()

    fun getGifticonData(id: Long): GifticonData? {
        return gifticonDataMap[id]
    }

    private val _gifticonDataMapFlow = MutableSharedFlow<Map<Long, GifticonData>>(replay = 1)
    val gifticonDataMapFlow = _gifticonDataMapFlow.asSharedFlow()

    val validGifticonCount = gifticonDataMapFlow
        .map { map -> map.count { !it.value.isInvalid } }
        .distinctUntilChanged()

    val isRegisterActivated = gifticonDataMapFlow
        .map { map -> map.values.any { !it.isInvalid } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun updateGifticonData(
        selectedItem:GalleryImage? = selectedGifticon.value,
        editData: EditData,
    ) {
        selectedItem ?: return
        val data = gifticonDataMap[selectedItem.id] ?: return
        Log.d("TEST", "updateGifticonData : $editData")

        if (!editData.isModified(data)) {
            return
        }

        Log.d("TEST", "updateGifticonData success : $editData")
        gifticonDataMap[selectedItem.id] = editData.updatedGifticon(data)
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }
    }

    val selectedGifticonData = combine(
        selectedGifticon,
        gifticonDataMapFlow,
    ) { selected, gifticonMap ->
        selected ?: return@combine null
        gifticonMap[selected.id]
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedGifticonDataFlow = selectedGifticonData.filterNotNull()

    val editorPropertyChipList = selectedGifticonDataFlow.map { data ->
        EditType.entries.filter { type ->
            when (type) {
                EditType.MEMO -> false
                EditType.BALANCE -> data.isCashCard
                else -> true
            }
        }.map {
            EditorChip.Property(it)
        }
    }.distinctUntilChanged()

    private val _recognizeLoading = MutableStateFlow(false)
    val recognizeLoading = _recognizeLoading.asStateFlow()

    init {
        _recognizeLoading.value = true
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(emptyMap())
            galleryImage.value.map { gallery ->
                launch {
                    val data = recognizeGifticonUseCase(gallery).getOrNull()
                        .toGifticonData(gallery.contentUri)
                    gifticonDataMap[gallery.id] = data
                }
            }.joinAll()
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }.also {
            it.invokeOnCompletion {
                _recognizeLoading.value = false
            }
        }
    }
}