package com.lighthouse.beep

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.auth.GetAuthInfoUseCase
import com.lighthouse.beep.domain.usecase.setting.ClearDeviceConfigUseCase
import com.lighthouse.beep.domain.usecase.setting.GetDeviceConfigUseCase
import com.lighthouse.beep.navigation.TopLevelDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    val isLogin = combine(
        authInfo,
        deviceConfig,
    ) { authInfo, deviceConfig ->
        authInfo.userUid != "" && authInfo.userUid == deviceConfig.authInfo.userUid
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val startDestination = flow {
        Log.d("TEST", "call startDestination")
        val authInfo = authInfo.first()
        val deviceConfig = deviceConfig.first()
        val destination = when {
            authInfo.userUid != "" &&
                authInfo.userUid == deviceConfig.authInfo.userUid -> {
                TopLevelDestination.MAIN
            }

            else -> TopLevelDestination.LOGIN
        }
        emit(destination)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TopLevelDestination.NONE)
}
