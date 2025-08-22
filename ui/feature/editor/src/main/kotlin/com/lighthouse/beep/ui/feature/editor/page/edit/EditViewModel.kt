package com.lighthouse.beep.ui.feature.editor.page.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorGifticonInfo
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import com.lighthouse.beep.ui.feature.editor.model.toData
import com.lighthouse.beep.ui.feature.editor.model.toEditInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gifticonRepository: GifticonRepository,
): ViewModel() {

    val gifticonId = EditParam.getGifticonId(savedStateHandle)

    private var originEditInfo: GifticonEditInfo? = null

    private val _editorGifticonInfo = MutableStateFlow<EditorGifticonInfo?>(null)
    val editorGifticonInfo = _editorGifticonInfo.asStateFlow()

    private val _gifticonData = MutableStateFlow<GifticonData?>(null)
    val gifticonData = _gifticonData.asStateFlow()

    val validGifticon = gifticonData.map {
        it?.isInvalid == false
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


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

    val editorPropertyChipList = gifticonData.filterNotNull().map { data ->
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
    fun selectInvalidEditType() {
        val data = gifticonData.value ?: return
        val type = EditType.entries.find { it.isInvalid(data) } ?: return

        selectEditorChip(EditorChip.Property(type))
    }

    fun updateGifticonData(
        editData: EditData,
    ) {
        val data = gifticonData.value ?: return
        if (!editData.isModified(data)) {
            return
        }
        _gifticonData.value = editData.updatedGifticon(data)
    }

    suspend fun editGifticon() {
        val data = gifticonData.value ?: return
        gifticonRepository.updateGifticon(gifticonId, data.toEditInfo())
    }

    init {
        viewModelScope.launch {
            originEditInfo = gifticonRepository.getGifticonEditInfo(BeepAuth.userUid, gifticonId)
            _editorGifticonInfo.value = originEditInfo?.let {
                EditorGifticonInfo(gifticonId, it.originUri)
            }
            _gifticonData.value = originEditInfo?.toData()
        }
    }
}