package com.lighthouse.ui

import androidx.lifecycle.ViewModel
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase
) : ViewModel() {

    fun isLogin() = isLoginUserUseCase()
}
