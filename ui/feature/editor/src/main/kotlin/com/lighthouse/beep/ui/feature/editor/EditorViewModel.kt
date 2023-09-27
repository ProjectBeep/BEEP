package com.lighthouse.beep.ui.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.model.gallery.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}