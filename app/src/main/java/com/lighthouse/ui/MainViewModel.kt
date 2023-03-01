package com.lighthouse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase
) : ViewModel() {

    private val _isLoginFlow = MutableSharedFlow<Boolean>()
    val isLoginFlow = _isLoginFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            isLoginUserUseCase().collect { isLogin ->
                _isLoginFlow.emit(isLogin)
            }
        }
    }
}
