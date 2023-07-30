package com.lighthouse.beep.ui.feature.login.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.user.SignInUseCase
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.ui.feature.login.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    val items = listOf(
        LoginData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim1,
        ),
        LoginData(
            titleRes = R.string.recognize_name,
            descriptionRes = R.string.recognize_description,
            lottieRes = R.raw.lottie_anim2,
        ),
        LoginData(
            titleRes = R.string.map_name,
            descriptionRes = R.string.map_description,
            lottieRes = R.raw.lottie_anim3,
        ),
    )

    private val _loadingState = MutableStateFlow<Boolean>(false)
    val loadingState = _loadingState.asStateFlow()

    private val _loginEvent = MutableSharedFlow<Result<Unit>>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun requestLogin(provider: AuthProvider, accessToken: String = "") {
        viewModelScope.launch {
            _loadingState.value = true
            _loginEvent.emit(signInUseCase.invoke(provider, accessToken))
            _loadingState.value = false
        }
    }
}
