package com.lighthouse.beep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.auth.GetAuthInfoUseCase
import com.lighthouse.beep.domain.usecase.setting.GetDeviceConfigUseCase
import com.lighthouse.beep.domain.usecase.user.GetUserConfigUseCase
import com.lighthouse.beep.domain.usecase.user.UpdateAuthInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    getAuthInfoUseCase: GetAuthInfoUseCase,
    getUserConfigUseCase: GetUserConfigUseCase,
    getDeviceConfigUseCase: GetDeviceConfigUseCase,
    updateAuthInfoUseCase: UpdateAuthInfoUseCase,
) : ViewModel() {

    val uiState = getAuthInfoUseCase().flatMapLatest {
        updateAuthInfoUseCase(it)
        combine(
            getUserConfigUseCase(),
            getDeviceConfigUseCase(),
        ) { userConfig, deviceConfig ->
            MainUiState.Success(userConfig, deviceConfig)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState.Loading,
    )
}
