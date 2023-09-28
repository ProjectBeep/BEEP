package com.lighthouse.beep.ui.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.PropertyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(){

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

    val editorPropertyChipList = PropertyType.entries.map {
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
}