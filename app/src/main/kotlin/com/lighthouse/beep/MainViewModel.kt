package com.lighthouse.beep

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    deviceRepository: DeviceRepository,
    userRepository: UserRepository,
) : ViewModel() {

    val uiState = combine(
        BeepAuth.authInfoFlow.onEach {
            Log.d("STOP_TEST", "authInfoFlow : $it")
        }.filterNotNull(),
        userRepository.userConfig.onEach {
            Log.d("STOP_TEST", "userConfig : $it")
        },
        deviceRepository.deviceConfig.onEach {
            Log.d("STOP_TEST", "deviceConfig : $it")
        },
    ) { authInfo, userConfig, deviceConfig ->
        Log.d("STOP_TEST", "state : ${MainUiState.Success(authInfo, userConfig, deviceConfig)}")
        MainUiState.Success(authInfo, userConfig, deviceConfig)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState.Loading,
    )
}
