package com.lighthouse.beep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.auth.GetAuthInfoUseCase
import com.lighthouse.beep.domain.usecase.setting.ClearDeviceConfigUseCase
import com.lighthouse.beep.domain.usecase.setting.GetDeviceConfigUseCase
import com.lighthouse.beep.navigation.TopLevelDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    getAuthInfoUseCase: GetAuthInfoUseCase,
    getDeviceConfigUseCase: GetDeviceConfigUseCase,
    clearDeviceConfigUseCase: ClearDeviceConfigUseCase,
) : ViewModel() {

    private val authInfo = getAuthInfoUseCase()

    private val deviceConfig = getDeviceConfigUseCase()

    private val _topDestination = MutableStateFlow(TopLevelDestination.NONE)
    val topDestination = _topDestination.asStateFlow()

    val isInit = flow {
        val auth = authInfo.first()
        val device = deviceConfig.first()
        _topDestination.value = when {
            auth.userUid != device.authInfo.userUid -> {
                clearDeviceConfigUseCase()
                TopLevelDestination.LOGIN
            }

            auth.userUid != "" && auth.userUid == device.authInfo.userUid -> {
                TopLevelDestination.MAIN
            }

            else -> TopLevelDestination.LOGIN
        }
        emit(true)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
