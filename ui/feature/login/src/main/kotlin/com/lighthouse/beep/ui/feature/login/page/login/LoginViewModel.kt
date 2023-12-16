package com.lighthouse.beep.ui.feature.login.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.setting.GetBeepGuideUseCase
import com.lighthouse.beep.domain.usecase.setting.SetBeepGuideUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    getBeepGuideUseCase: GetBeepGuideUseCase,
    private val setBeepGuideUseCase: SetBeepGuideUseCase,
): ViewModel() {

    val isShownPermissionPage = getBeepGuideUseCase().map { it.permission }

    fun setShownPermissionPage(value: Boolean) {
        viewModelScope.launch {
            setBeepGuideUseCase {
                it.copy(permission = value)
            }
        }
    }
}