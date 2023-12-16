package com.lighthouse.beep.ui.feature.editor.page.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.setting.GetBeepGuideUseCase
import com.lighthouse.beep.domain.usecase.setting.SetBeepGuideUseCase
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditorCropViewModel @Inject constructor(
    private val getBeepGuideUseCase: GetBeepGuideUseCase,
    private val setBeepGuideUseCase: SetBeepGuideUseCase,
): ViewModel() {


    private val _cropImageMode = MutableStateFlow(CropImageMode.NONE)
    val cropImageMode = _cropImageMode.asStateFlow()

    fun setCropImageMode(mode: CropImageMode) {
        _cropImageMode.value = mode
    }

    val isShowCropImagePenGuide = getBeepGuideUseCase().map { it.cropImagePen }

    fun setShowCropImagePenGuide(value: Boolean) {
        viewModelScope.launch {
            setBeepGuideUseCase {
                it.copy(cropImagePen = value)
            }
        }
    }
}