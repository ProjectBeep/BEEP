package com.lighthouse.features.intro.ui

import androidx.lifecycle.ViewModel
import com.lighthouse.domain.usecase.user.IsLoginUserUseCase
import com.lighthouse.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class IntroViewModel @Inject constructor(
    private val isLoginUserUseCase: IsLoginUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    suspend fun isLogin() = isLoginUserUseCase()

    suspend fun login(): Result<Unit> {
        return loginUseCase()
    }

    fun setLoading(loading: Boolean) {
        _loading.value = loading
    }
}
