package com.lighthouse.features.intro.ui

import androidx.lifecycle.ViewModel
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import com.lighthouse.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    suspend fun isLogin() = isLoginUserUseCase()

    suspend fun login(): Result<Unit> {
        return loginUseCase()
    }
}
