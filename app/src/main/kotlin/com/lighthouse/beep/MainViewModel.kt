package com.lighthouse.beep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val uiState = BeepAuth.authInfoFlow.filterNotNull().flatMapLatest { newAuthInfo ->
        userRepository.setAuthInfo { newAuthInfo }
        combine(
            userRepository.userConfig,
            deviceRepository.deviceConfig,
        ) { userConfig, deviceConfig ->
            MainUiState.Success(userConfig, deviceConfig)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState.Loading,
    )
}
