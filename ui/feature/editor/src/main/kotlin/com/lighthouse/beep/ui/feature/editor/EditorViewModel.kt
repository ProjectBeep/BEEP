package com.lighthouse.beep.ui.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBalanceUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeExpiredUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeGifticonUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeTextUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import com.lighthouse.beep.ui.feature.editor.model.EditType
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
    private val recognizeTextUseCase: RecognizeTextUseCase,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
    private val recognizeBalanceUseCase: RecognizeBalanceUseCase,
    private val recognizeExpiredUseCase: RecognizeExpiredUseCase,
) : ViewModel() {

    private val _galleryImage = MutableStateFlow(EditorParam.getGalleryList(savedStateHandle))
    val galleryImage = _galleryImage.asStateFlow()

    fun deleteItem(item: GalleryImage) {
        _galleryImage.value = _galleryImage.value.filter { it.id != item.id }
    }

    private val _selectedGifticon = MutableStateFlow(_galleryImage.value.firstOrNull())
    val selectedGifticon = _selectedGifticon.asStateFlow()

    fun selectGifticon(item: GalleryImage) {
        _selectedGifticon.value = item
    }

    val editorPropertyChipList = EditType.entries.map {
        EditorChip.Property(it)
    }

    private val _selectedEditorChip = MutableStateFlow<EditorChip>(EditorChip.Preview)
    val selectedEditorChip = _selectedEditorChip.asStateFlow()

    val isSelectPreview = selectedEditorChip
        .map { it is EditorChip.Preview }
        .distinctUntilChanged()

    fun selectEditorChip(item: EditorChip) {
        _selectedEditorChip.value = item
    }

    private val gifticonDataMap = mutableMapOf<Long, GifticonData>()

    private val _gifticonDataMapFlow = MutableSharedFlow<Map<Long, GifticonData>>(replay = 1)
    val gifticonDataMapFlow = _gifticonDataMapFlow.asSharedFlow()

    val isRegisterActivated = gifticonDataMapFlow
        .map { map -> map.values.any { !it.isInvalid } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun updateGifticonData(
        selectedItem: GalleryImage? = selectedGifticon.value,
        editData: EditData,
    ) {
        selectedItem ?: return
        val data = gifticonDataMap[selectedItem.id] ?: return
        if (!editData.isModified(data)) {
            return
        }
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

    val maxMemoLength = 20

    private val _recognizeLoading = MutableStateFlow(false)
    val recognizeLoading = _recognizeLoading.asStateFlow()

    init {
        _recognizeLoading.value = true
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(emptyMap())
            galleryImage.value.map { gallery ->
                launch {
                    val data = recognizeGifticonUseCase(gallery).getOrNull().toGifticonData(gallery.contentUri)
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