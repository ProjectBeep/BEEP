package com.lighthouse.features.setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.domain.usecase.setting.GetNotificationOptionUseCase
import com.lighthouse.domain.usecase.setting.GetSecurityOptionUseCase
import com.lighthouse.domain.usecase.setting.SetNotificationOptionUseCase
import com.lighthouse.domain.usecase.setting.SetSecurityOptionUseCase
import com.lighthouse.domain.usecase.user.IsGuestUseCase
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.ext.settingGroup
import com.lighthouse.features.setting.ext.settingItems
import com.lighthouse.features.setting.model.SettingMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    isGuestUseCase: IsGuestUseCase,
    getNotificationOptionUseCase: GetNotificationOptionUseCase,
    getSecurityOptionUseCase: GetSecurityOptionUseCase,
    private val setNotificationOptionUseCase: SetNotificationOptionUseCase,
    private val setSecurityOptionUseCase: SetSecurityOptionUseCase
) : ViewModel() {

    private val settingGroup = settingGroup {
        setTitle(UIText.StringResource(R.string.subtitle_setting))
        addStateSwitch(SettingMenu.IMMINENT_NOTIFICATION)
        addStateButton(SettingMenu.SECURITY)
        addStateButton(SettingMenu.LOCATION)
    }

    private val userGroup = settingGroup {
        setTitle(UIText.StringResource(R.string.subtitle_user))
        addStateButton(SettingMenu.SIGN_IN)
        addStateButton(SettingMenu.SIGN_OUT)
        addStateButton(SettingMenu.WITHDRAWAL)
    }

    private val etcGroup = settingGroup {
        setTitle(UIText.StringResource(R.string.subtitle_etc))
        addButton(SettingMenu.COFFEE)
        addButton(SettingMenu.TERMS_OF_USE)
        addButton(SettingMenu.PERSONAL_INFO_POLICY)
        addButton(SettingMenu.OPEN_SOURCE_LICENSE)
    }

    private val menus = settingItems {
        addButton(SettingMenu.USED_GIFTICON)
        addGroup(settingGroup)
        addGroup(userGroup)
        addGroup(etcGroup)
    }

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

    fun setNotificationEnable(enable: Boolean) {
        viewModelScope.launch {
            setNotificationOptionUseCase(enable)
        }
    }

    val securityOption = getSecurityOptionUseCase().map {
        when (it.getOrDefault(SecurityOption.NONE)) {
            SecurityOption.NONE -> UIText.StringResource(R.string.security_none)
            SecurityOption.PIN -> UIText.StringResource(R.string.security_pin)
            SecurityOption.FINGERPRINT -> UIText.StringResource(R.string.security_fingerprint)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UIText.Empty)

    fun setSecurityOption(option: SecurityOption) {
        viewModelScope.launch {
            setSecurityOptionUseCase(option)
        }
    }
}
