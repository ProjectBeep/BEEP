package com.lighthouse.features.intro.ui

import androidx.lifecycle.ViewModel
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import com.lighthouse.domain.usecase.user.LoginUseCase
import com.lighthouse.features.intro.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class IntroViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.None)
    val signInState = _signInState.asStateFlow()

    suspend fun isLogin() = isLoginUserUseCase()

    suspend fun login(): Result<Unit> {
        return loginUseCase()
    }

    fun setState(state: SignInState) {
        _signInState.value = state
    }
}
