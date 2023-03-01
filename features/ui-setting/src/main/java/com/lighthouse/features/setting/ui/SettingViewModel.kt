package com.lighthouse.features.setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.model.user.SecurityOption
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.domain.usecase.setting.GetNotificationOptionUseCase
import com.lighthouse.domain.usecase.setting.GetSecurityOptionUseCase
import com.lighthouse.domain.usecase.setting.SetNotificationOptionUseCase
import com.lighthouse.domain.usecase.user.IsGuestUseCase
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.ext.settingGroup
import com.lighthouse.features.setting.ext.settingItems
import com.lighthouse.features.setting.model.SettingGroup
import com.lighthouse.features.setting.model.SettingMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val isGuestUseCase: IsGuestUseCase,
    getNotificationOptionUseCase: GetNotificationOptionUseCase,
    getSecurityOptionUseCase: GetSecurityOptionUseCase,
    private val setNotificationOptionUseCase: SetNotificationOptionUseCase
) : ViewModel() {

    private val notificationEnable = getNotificationOptionUseCase().map {
        it.getOrDefault(false)
    }

    fun setNotificationEnable(enable: Boolean) {
        viewModelScope.launch {
            setNotificationOptionUseCase(enable)
        }
    }

    private val securityOption = getSecurityOptionUseCase().map {
        when (it.getOrDefault(SecurityOption.NONE)) {
            SecurityOption.NONE -> UIText.StringResource(R.string.security_none)
            SecurityOption.PIN -> UIText.StringResource(R.string.security_pin)
            SecurityOption.FINGERPRINT -> UIText.StringResource(R.string.security_fingerprint)
            else -> UIText.Empty
        }
    }

    private val locationEnableText = MutableStateFlow<UIText>(UIText.Empty)

    fun setLocationEnable(enable: Boolean) {
        val stringRes = if (enable) {
            R.string.location_permission_allowed
        } else {
            R.string.location_permission_not_allowed
        }
        locationEnableText.value = UIText.StringResource(stringRes)
    }

    private val configGroup = combine(
        notificationEnable,
        securityOption,
        locationEnableText
    ) { notificationEnable, securityOption, locationEnableText ->
        settingGroup {
            setTitle(UIText.StringResource(R.string.subtitle_config))
            addStateSwitch(SettingMenu.IMMINENT_NOTIFICATION, notificationEnable)
            addStateButton(SettingMenu.SECURITY, securityOption)
            addStateButton(SettingMenu.LOCATION, locationEnableText)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingGroup.Empty)

    private val userGroup = isGuestUseCase().map { isGuest ->
        settingGroup {
            setTitle(UIText.StringResource(R.string.subtitle_user))
            if (isGuest) {
                addStateButton(SettingMenu.SIGN_IN)
            } else {
                addStateButton(SettingMenu.SIGN_OUT)
            }
            addStateButton(SettingMenu.WITHDRAWAL)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingGroup.Empty)

    val settingMenus = combine(
        configGroup,
        userGroup
    ) { configGroup, userGroup ->
        settingItems {
            addButton(SettingMenu.USED_GIFTICON)
            addGroup(configGroup)
            addGroup(userGroup)
            addGroup(
                settingGroup {
                    setTitle(UIText.StringResource(R.string.subtitle_etc))
                    addButton(SettingMenu.COFFEE)
                    addButton(SettingMenu.TERMS_OF_USE)
                    addButton(SettingMenu.PERSONAL_INFO_POLICY)
                    addButton(SettingMenu.OPEN_SOURCE_LICENSE)
                }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
}
