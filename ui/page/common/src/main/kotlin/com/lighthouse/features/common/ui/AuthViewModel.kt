package com.lighthouse.features.common.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.lighthouse.beep.model.auth.AuthProvider
import com.lighthouse.beep.model.auth.exception.FailedConnectException
import com.lighthouse.beep.model.auth.exception.FailedSaveLoginUserException
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.core.utils.flow.MutableEventFlow
import com.lighthouse.core.utils.flow.asEventFlow
import com.lighthouse.domain.usecase.user.SignInUseCase
import com.lighthouse.domain.usecase.user.SignOutUseCase
import com.lighthouse.domain.usecase.user.WithdrawalUseCase
import com.lighthouse.features.common.R
import com.lighthouse.features.common.model.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<MessageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _loginLoading = MutableStateFlow(false)
    val loginLoading = _loginLoading.asStateFlow()

    fun login(provider: AuthProvider, credential: AuthCredential) {
        viewModelScope.launch {
            endLogin(signInUseCase(provider, credential).exceptionOrNull())
        }
    }

    fun startLogin() {
        _loginLoading.value = true
    }

    fun endLogin(throwable: Throwable? = null) {
        endLogin(throwable as? Exception)
    }

    fun endLogin(exception: Exception? = null) {
        _loginLoading.value = false
        viewModelScope.launch {
            val stringRes = when (exception) {
                null -> R.string.login_success
                is FailedSaveLoginUserException -> R.string.error_save_login_user
                is FailedConnectException -> R.string.google_connect_fail
                else -> R.string.error_unknown
            }
            _eventFlow.emit(MessageEvent.SnackBar(UIText.StringResource(stringRes)))
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val exception = signOutUseCase().exceptionOrNull()
            if (exception != null) {
                _eventFlow.emit(
                    MessageEvent.SnackBar(
                        UIText.StringResource(R.string.error_sign_out)
                    )
                )
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            val exception = withdrawalUseCase().exceptionOrNull()
            if (exception != null) {
                _eventFlow.emit(
                    MessageEvent.SnackBar(
                        UIText.StringResource(R.string.error_withdrawal)
                    )
                )
            }
        }
    }
}