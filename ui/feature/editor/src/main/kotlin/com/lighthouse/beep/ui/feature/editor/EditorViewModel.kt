package com.lighthouse.beep.ui.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.model.gallery.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    val galleryImage = mutableListOf<GalleryImage>().apply {
        addAll(EditorParam.getGalleryList(savedStateHandle))
    }
}