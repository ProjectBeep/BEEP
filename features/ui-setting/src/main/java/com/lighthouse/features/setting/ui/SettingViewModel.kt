package com.lighthouse.features.setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.domain.usecase.setting.GetNotificationOptionUseCase
import com.lighthouse.domain.usecase.setting.GetSecurityOptionUseCase
import com.lighthouse.domain.usecase.user.IsGuestUseCase
import com.lighthouse.features.setting.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    isGuestUseCase: IsGuestUseCase,
    getNotificationOptionUseCase: GetNotificationOptionUseCase,
    getSecurityOptionUseCase: GetSecurityOptionUseCase
) : ViewModel() {

    private val isGuest = isGuestUseCase()

    val isVisibleSignIn = isGuest.map {
        it
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isVisibleSignOut = isGuest.map {
        !it
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificationEnable = getNotificationOptionUseCase().map {
        it.getOrDefault(false)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val securityOption = getSecurityOptionUseCase().map {
        when (it.getOrDefault(SecurityOption.NONE)) {
            SecurityOption.NONE -> UIText.StringResource(R.string.security_none)
            SecurityOption.PIN -> UIText.StringResource(R.string.security_pin)
            SecurityOption.FINGERPRINT -> UIText.StringResource(R.string.security_fingerprint)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UIText.Empty)
}
