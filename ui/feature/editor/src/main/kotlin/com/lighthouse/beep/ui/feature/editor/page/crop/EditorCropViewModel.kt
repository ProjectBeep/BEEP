package com.lighthouse.beep.ui.feature.editor.page.crop

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class EditorCropViewModel: ViewModel() {

    private val _cropImageMode = MutableStateFlow(CropImageMode.NONE)
    val cropImageMode = _cropImageMode.asStateFlow()

    fun setCropImageMode(mode: CropImageMode) {
        _cropImageMode.value = mode
    }

    fun toggleCropImageMode() {
        _cropImageMode.value = when(_cropImageMode.value) {
            CropImageMode.DRAW_PEN -> CropImageMode.DRAG_WINDOW
            CropImageMode.DRAG_WINDOW -> CropImageMode.DRAW_PEN
            CropImageMode.NONE -> CropImageMode.NONE
        }
    }
}