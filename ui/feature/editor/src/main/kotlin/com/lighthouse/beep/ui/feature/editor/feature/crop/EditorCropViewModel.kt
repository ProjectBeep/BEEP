package com.lighthouse.beep.ui.feature.editor.feature.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditorCropViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
): ViewModel() {


    private val _cropImageMode = MutableStateFlow(CropImageMode.NONE)
    val cropImageMode = _cropImageMode.asStateFlow()

    fun setCropImageMode(mode: CropImageMode) {
        _cropImageMode.value = mode
    }

    val isShownCropImagePenGuide = deviceRepository.deviceConfig.map{
        it.beepGuide.cropImagePen
    }

    fun setShownCropImagePenGuide(value: Boolean) {
        viewModelScope.launch {
            deviceRepository.setBeepGuide {
                it.copy(cropImagePen = value)
            }
        }
    }
}