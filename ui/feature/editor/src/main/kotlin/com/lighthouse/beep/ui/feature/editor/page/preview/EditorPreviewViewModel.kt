package com.lighthouse.beep.ui.feature.editor.page.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighthouse.beep.ui.feature.editor.EditorSelectGifticonDataDelegate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class EditorPreviewViewModel constructor(
    editorSelectGifticonDataDelegate: EditorSelectGifticonDataDelegate
) : ViewModel(), EditorSelectGifticonDataDelegate by editorSelectGifticonDataDelegate {

    companion object {
        fun factory(delegate: EditorSelectGifticonDataDelegate) = viewModelFactory {
            initializer {
                EditorPreviewViewModel(delegate)
            }
        }
    }

    val thumbnailUri = selectedGifticonDataFlow
        .map { it.originUri }
        .distinctUntilChanged()

    val thumbnailCropData = selectedGifticonDataFlow
        .map { it.thumbnailCropData }
        .distinctUntilChanged()

    val gifticonName = selectedGifticonDataFlow
        .map { it.name }
        .distinctUntilChanged()

    val brandName = selectedGifticonDataFlow
        .map { it.brand }
        .distinctUntilChanged()

    val expired = selectedGifticonDataFlow
        .map { it.displayExpired }
        .distinctUntilChanged()

    val memo = selectedGifticonDataFlow
        .map { it.memo }
        .distinctUntilChanged()
}