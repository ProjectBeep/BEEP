package com.lighthouse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase
) : ViewModel() {

    fun isLogin() = isLoginUserUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
