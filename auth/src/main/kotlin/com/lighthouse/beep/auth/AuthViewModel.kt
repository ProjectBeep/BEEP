package com.lighthouse.beep.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.core.common.utils.log.MSLog
import com.lighthouse.beep.domain.usecase.auth.SignInAsGuestUseCase
import com.lighthouse.beep.domain.usecase.auth.SignInWithCustomTokenUseCase
import com.lighthouse.beep.domain.usecase.auth.WithdrawalAccountUseCase
import com.lighthouse.beep.model.user.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithCustomTokenUseCase: SignInWithCustomTokenUseCase,
    private val signInAsGuestUseCase: SignInAsGuestUseCase,
    private val withdrawalAccountUseCase: WithdrawalAccountUseCase,
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _loadingPostpone = MutableStateFlow(false)
    val loadingPostpone = _loadingPostpone.asStateFlow()

    fun setLoadingPostpone(value: Boolean) {
        MSLog.d(TAG, "로딩 상태 변경: $value")
        viewModelScope.launch {
            _loadingPostpone.emit(value)
        }
    }

    suspend fun requestFirebaseSignInWithCustomToken(
        provider: AuthProvider,
        accessToken: String
    ) {
        MSLog.d(TAG, "Firebase Custom Token 인증 시작 - provider: $provider")
        signInWithCustomTokenUseCase(provider, accessToken)
        MSLog.d(TAG, "Firebase Custom Token 인증 완료")
    }

    suspend fun requestGuestSignIn() {
        MSLog.d(TAG, "Guest 로그인 시작")
        signInAsGuestUseCase()
        MSLog.d(TAG, "Guest 로그인 완료")
    }

    suspend fun withdrawalAndDeleteUserInfo() {
        MSLog.d(TAG, "회원 탈퇴 및 사용자 정보 삭제 시작")
        withdrawalAccountUseCase()
        MSLog.d(TAG, "회원 탈퇴 및 사용자 정보 삭제 완료")
    }
}