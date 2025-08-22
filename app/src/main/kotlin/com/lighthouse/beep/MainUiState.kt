package com.lighthouse.beep

import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.UserConfig

sealed interface MainUiState {

    data object Loading : MainUiState

    data class Success(
        val authInfo: AuthInfo,
        val userConfig: UserConfig,
        val deviceConfig: DeviceConfig,
    ) : MainUiState
}