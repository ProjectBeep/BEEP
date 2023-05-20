package com.lighthouse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.user.IsLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    isLoginUserUseCase: IsLoginUserUseCase,
) : ViewModel() {

    private val _isLoginFlow = MutableSharedFlow<Boolean>()
    val isLoginFlow = _isLoginFlow.asSharedFlow()

    init {
        isLoginUserUseCase()
            .onEach { _isLoginFlow.emit(it) }
            .launchIn(viewModelScope)
    }
}
