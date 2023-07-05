package com.lighthouse.beep

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor() : ViewModel() {

    private val _isLoginFlow = MutableSharedFlow<Boolean>()
    val isLoginFlow = _isLoginFlow.asSharedFlow()

    init {
//        isLoginUserUseCase()
//            .onEach { _isLoginFlow.emit(it) }
//            .launchIn(viewModelScope)
    }
}
