package com.lighthouse.beep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.auth.GetAuthInfoUseCase
import com.lighthouse.beep.domain.usecase.setting.ClearDeviceConfigUseCase
import com.lighthouse.beep.domain.usecase.setting.GetDeviceConfigUseCase
import com.lighthouse.beep.navigation.TopLevelDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    val topDestination = combine(
        authInfo,
        deviceConfig,
    ) { authInfo, deviceConfig ->
        when {
            authInfo.userUid == "" ||
                authInfo.userUid != deviceConfig.authInfo.userUid -> {
                clearDeviceConfigUseCase()
                TopLevelDestination.LOGIN
            }

            !deviceConfig.shownGuidePage.permission -> {
                TopLevelDestination.GUIDE_PERMISSION
            }

            authInfo.userUid == deviceConfig.authInfo.userUid -> {
                TopLevelDestination.MAIN
            }

            else -> TopLevelDestination.LOGIN
        }
        TopLevelDestination.GUIDE_PERMISSION
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TopLevelDestination.NONE)
}
