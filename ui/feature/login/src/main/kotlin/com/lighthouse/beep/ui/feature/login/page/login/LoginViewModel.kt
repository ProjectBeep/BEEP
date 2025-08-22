package com.lighthouse.beep.ui.feature.login.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.data.repository.device.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
): ViewModel() {

    val isShownPermissionPage = deviceRepository.deviceConfig.map {
        it.beepGuide.permission
    }

    fun setShownPermissionPage(value: Boolean) {
        viewModelScope.launch {
            deviceRepository.setBeepGuide {
                it.copy(permission = value)
            }
        }
    }
}