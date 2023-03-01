package com.lighthouse.auth.google.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.auth.google.R
import com.lighthouse.auth.google.exception.FailedConnectException
import com.lighthouse.auth.google.model.GoogleAuthEvent
import com.lighthouse.beep.model.exception.auth.FailedSaveLoginUserException
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.core.utils.flow.MutableEventFlow
import com.lighthouse.core.utils.flow.asEventFlow
import com.lighthouse.domain.usecase.user.GetUserIdUseCase
import com.lighthouse.domain.usecase.user.LoginUseCase
import com.lighthouse.domain.usecase.user.SignOutUseCase
import com.lighthouse.domain.usecase.user.TransferDataUseCase
import com.lighthouse.domain.usecase.user.WithdrawalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val loginUseCase: LoginUseCase,
    private val transferDataUseCase: TransferDataUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<GoogleAuthEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _signInLoading = MutableStateFlow(false)
    val signInLoading = _signInLoading.asStateFlow()

    fun getUserId() = getUserIdUseCase()

    fun login() {
        viewModelScope.launch {
            finishSignIn(loginUseCase().exceptionOrNull())
        }
    }

    fun transferData(oldUserId: String) {
        viewModelScope.launch {
            finishSignIn(transferDataUseCase(oldUserId).exceptionOrNull())
        }
    }

    fun startSignIn() {
        _signInLoading.value = true
    }

    fun finishSignIn(throwable: Throwable? = null) {
        finishSignIn(throwable as? Exception)
    }

    private fun finishSignIn(exception: Exception? = null) {
        _signInLoading.value = false
        viewModelScope.launch {
            val stringRes = when (exception) {
                null -> R.string.login_success
                is FailedSaveLoginUserException -> R.string.error_save_login_user
                is FailedConnectException -> R.string.google_connect_fail
                else -> R.string.error_unknown
            }
            _eventFlow.emit(GoogleAuthEvent.SnackBar(UIText.StringResource(stringRes)))
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val exception = signOutUseCase().exceptionOrNull()
            if (exception != null) {
                _eventFlow.emit(
                    GoogleAuthEvent.SnackBar(
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
                    GoogleAuthEvent.SnackBar(
                        UIText.StringResource(R.string.error_withdrawal)
                    )
                )
            }
        }
    }
}
