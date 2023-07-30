package com.lighthouse.beep

import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filterIsInstance

sealed interface MainUiState {

    data object Loading : MainUiState

    data class Success(
        val userConfig: UserConfig,
        val deviceConfig: DeviceConfig,
    ) : MainUiState
}

// startDestination 설정과 겹치지 않기 위해서 로그인할 때까지 drop
// 로그인한 경우 해당 상태도 의미가 없기 때문에 drop(1)
// provider 로 distinctUntilChanged 하여
// 로그인이 해제 된경우 로그인 화면으로 이동
internal fun Flow<MainUiState>.detectLoginRequirement(): Flow<MainUiState.Success> {
    return filterIsInstance<MainUiState.Success>()
        .dropWhile { it.userConfig.authInfo.provider != AuthProvider.NONE }
        .drop(1)
        .distinctUntilChangedBy {
            it.userConfig.authInfo.provider
        }
}
