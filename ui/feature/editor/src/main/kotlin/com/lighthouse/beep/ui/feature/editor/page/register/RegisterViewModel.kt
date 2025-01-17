package com.lighthouse.beep.ui.feature.editor.page.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.domain.usecase.recognize.RecognizeGifticonUseCase
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import com.lighthouse.beep.ui.feature.editor.model.toEditInfo
import com.lighthouse.beep.ui.feature.editor.model.toData
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
import kotlin.math.abs

@HiltViewModel
internal class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gifticonRepository: GifticonRepository,
    private val recognizeGifticonUseCase: RecognizeGifticonUseCase,
): ViewModel() {

    private val originGalleryImage = RegisterParam.getGalleryList(savedStateHandle)

    private val _galleryImage = MutableStateFlow(originGalleryImage)
    val galleryImage = _galleryImage.asStateFlow()

    private val _selectedGifticon = MutableStateFlow(_galleryImage.value.firstOrNull())
    val selectedGifticon = _selectedGifticon.asStateFlow()

    private val gifticonDataMap = mutableMapOf<Long, GifticonData>()

    fun getGifticonData(id: Long): GifticonData? {
        return gifticonDataMap[id]
    }

    private val _gifticonDataMapFlow = MutableSharedFlow<Map<Long, GifticonData>>(replay = 1)
    val gifticonDataMapFlow = _gifticonDataMapFlow.asSharedFlow()

    val validGifticonList = gifticonDataMapFlow.map { map ->
        map.filter { !it.value.isInvalid }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    fun selectGifticon(item: GalleryImage) {
        _selectedGifticon.value = item
    }

    fun selectGifticon(position: Int) {
        val item = galleryImage.value.getOrNull(position) ?: return
        selectGifticon(item)
    }

    val selectedGifticonData = combine(
        selectedGifticon,
        gifticonDataMapFlow,
    ) { selected, gifticonMap ->
        selected ?: return@combine null
        gifticonMap[selected.id]
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedGifticonDataFlow = selectedGifticonData.filterNotNull()

    private val _selectedEditorChip = MutableStateFlow<EditorChip>(EditorChip.Preview)
    val selectedEditorChip = _selectedEditorChip.asStateFlow()

    fun selectEditorChip(item: EditorChip) {
        _selectedEditorChip.value = item
    }

    val selectedEditorPage = selectedEditorChip.map { chip ->
        when (chip) {
            is EditorChip.Preview -> EditorPage.PREVIEW
            else -> EditorPage.CROP
        }
    }.distinctUntilChanged()

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

    fun deleteGalleryContent(id: Long) {
        val item = _galleryImage.value.find { it.id == id } ?: return
        deleteItem(item)
    }

    fun deleteItem(item: GalleryImage) {
        val oldList = galleryImage.value
        val index = oldList.indexOfFirst { it.id == item.id }

        val selectedItem = selectedGifticon.value
        val isDeleteSelectedItem = item.id == selectedItem?.id
        if (isDeleteSelectedItem) {
            val newSelectItem = when {
                index + 1 < oldList.size -> oldList[index + 1]
                index - 1 >= 0 -> oldList[index - 1]
                else -> null
            }
            if (newSelectItem != null) {
                selectGifticon(newSelectItem)
            }
        }

        _galleryImage.value = oldList.subList(0, index) + oldList.subList(index + 1, oldList.size)
        gifticonDataMap.remove(item.id)
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }

        if (isDeleteSelectedItem) {
            selectEditorChip(EditorChip.Preview)
        }
    }

    private fun restoreList(map: Map<Long, GifticonData>) {
        val oldList = galleryImage.value
        val newList = originGalleryImage.filter { origin ->
            oldList.find { it.id == origin.id } != null ||
                    map.containsKey(origin.id)
        }
        _galleryImage.value = newList
        map.forEach { (id, data) ->
            gifticonDataMap[id] = data
        }
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }
    }

    fun revertDeleteItem(item: GalleryImage, data: GifticonData?) {
        data ?: return
        restoreList(mapOf(item.id to data))
    }

    private fun deleteList(list: List<Long>) {
        val oldList = galleryImage.value
        val newList = oldList.filter { oldItem -> list.none { it == oldItem.id } }

        val selectedItem = selectedGifticon.value
        val isDeleteSelectedItem = list.any { it == selectedItem?.id }
        if (isDeleteSelectedItem) {
            val index = oldList.indexOfFirst { it.id == selectedItem?.id }
            val newSelect = oldList
                .mapIndexed { i, galleryImage -> abs(index - i) to galleryImage }
                .filter { (_, image) -> image.id !in list }
                .minByOrNull { it.first }

            if (newSelect != null) {
                selectGifticon(newSelect.second)
            }
        }

        _galleryImage.value = newList
        list.forEach {
            gifticonDataMap.remove(it)
        }
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }
        if (isDeleteSelectedItem) {
            selectEditorChip(EditorChip.Preview)
        }
    }

    private var recentInsertGifticonIdList = emptyList<Long>()

    suspend fun registerGifticon(map: Map<Long, GifticonData>) {
        recentInsertGifticonIdList = gifticonRepository.insertGifticonList(
            userId = BeepAuth.userUid,
            gifticonInfoList = map.map {
                it.value.toEditInfo()
            }
        )
        deleteList(map.map { it.key })
    }

    fun revertRegisterGifticon(map: Map<Long, GifticonData>) {
        restoreList(map)
        viewModelScope.launch {
            gifticonRepository.deleteGifticon(
                userId = BeepAuth.userUid,
                gifticonIdList = recentInsertGifticonIdList
            )
            recentInsertGifticonIdList = emptyList()
        }
    }

    fun selectInvalidEditType() {
        val selectedItem = selectedGifticon.value ?: return
        val data = getGifticonData(selectedItem.id) ?: return
        val type = EditType.entries.find { it.isInvalid(data) } ?: return

        selectEditorChip(EditorChip.Property(type))
    }

    private val _recognizeLoading = MutableStateFlow(false)
    val recognizeLoading = _recognizeLoading.asStateFlow()

    fun updateGifticonData(
        id: Long? = selectedGifticon.value?.id,
        editData: EditData,
    ) {
        id ?: return
        val data = gifticonDataMap[id] ?: return

        if (!editData.isModified(data)) {
            return
        }

        gifticonDataMap[id] = editData.updatedGifticon(data)
        viewModelScope.launch {
            _gifticonDataMapFlow.emit(gifticonDataMap)
        }
    }

    init {
        _recognizeLoading.value = true
        viewModelScope.launch {
            galleryImage.value.map { gallery ->
                launch {
                    recognizeGifticonUseCase(gallery.contentUri).onSuccess {
                        gifticonDataMap[gallery.id] = it.toData(
                            originUri = gallery.contentUri,
                            imagePath = gallery.imagePath,
                            imageAddedDate = gallery.dateAdded,
                        )
                    }
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