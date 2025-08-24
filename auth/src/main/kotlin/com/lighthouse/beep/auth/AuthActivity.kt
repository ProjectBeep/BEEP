package com.lighthouse.beep.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.lighthouse.beep.auth.client.GoogleClient
import com.lighthouse.beep.auth.model.OAuthTokenResult
import com.lighthouse.beep.core.common.utils.log.MSLog
import com.lighthouse.beep.core.ui.exts.dismiss
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.ui.dialog.progress.ProgressDialog
import com.lighthouse.beep.ui.dialog.progress.ProgressParam
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.lighthouse.beep.theme.R as ThemeR

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AuthActivity"
        const val RESULT_OK = 1001
        const val RESULT_CANCELED = 1002
        const val RESULT_FAILED = 1003
    }

    private val googleClient by lazy {
        GoogleClient(applicationContext)
    }



    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MSLog.d(TAG, "AuthActivity onCreate 시작")

        setUpCollectState()

        lifecycleScope.launch {
            val param = AuthParam.getParam(intent)
            MSLog.d(TAG, "Intent 파라미터 확인: $param")
            when(param) {
                is AuthParam.SignIn -> {
                    MSLog.d(TAG, "로그인 프로세스 시작 - provider: ${param.provider}")
                    signIn(param.provider)
                }
                is AuthParam.SignOut -> {
                    MSLog.d(TAG, "로그아웃 프로세스 시작")
                    signOut()
                }
                is AuthParam.Withdrawal -> {
                    MSLog.d(TAG, "회원탈퇴 프로세스 시작")
                    withdrawal()
                }
                is AuthParam.None -> {
                    MSLog.w(TAG, "유효하지 않은 파라미터 - Activity 종료")
                    finish()
                }
            }
        }
    }

    private fun setUpCollectState() {
        lifecycleScope.launch {
            viewModel.loadingPostpone.collect { isLoadingPostpone ->
                if (isLoadingPostpone) {
                    hideProgress()
                } else {
                    showProgress()
                }
            }
        }
    }

    private suspend fun signIn(provider: AuthProvider) {
        MSLog.d(TAG, "signIn 호출 - provider: $provider")
        when(provider) {
            AuthProvider.NAVER -> {
                MSLog.w(TAG, "네이버 로그인 비활성화 - FAILED 결과 반환")
                setResult(RESULT_FAILED)
                finish()
            }
            AuthProvider.KAKAO -> {
                MSLog.w(TAG, "카카오 로그인 비활성화 - FAILED 결과 반환")
                setResult(RESULT_FAILED)
                finish()
            }
            AuthProvider.GOOGLE -> {
                MSLog.d(TAG, "Google 로그인 시작")
                signInGoogle()
            }
            AuthProvider.GUEST -> {
                MSLog.d(TAG, "Guest 로그인 시작")
                signInGuest()
            }
            AuthProvider.NONE -> {
                MSLog.w(TAG, "NONE 프로바이더 - Activity 종료")
                finish()
            }
        }
    }

    private suspend fun signInWithTokenResult(provider: AuthProvider, tokenResult: OAuthTokenResult) {
        MSLog.d(TAG, "signInWithTokenResult 호출 - provider: $provider, tokenResult: ${tokenResult::class.simpleName}")
        when (tokenResult) {
            is OAuthTokenResult.Success -> {
                MSLog.d(TAG, "Access Token 획득 성공 - Firebase 인증 시작")
                runCatching {
                    val token = tokenResult.accessToken
                    MSLog.d(TAG, "Access Token: ${token.take(20)}...")
                    viewModel.requestFirebaseSignInWithCustomToken(provider, token)
                    MSLog.d(TAG, "Firebase 인증 성공 - RESULT_OK 설정")
                    setResult(RESULT_OK)
                }.onFailure { exception ->
                    MSLog.e(TAG, "Firebase 인증 실패 - RESULT_FAILED 설정", exception)
                    setResult(RESULT_FAILED)
                }
            }
            is OAuthTokenResult.Canceled -> {
                MSLog.d(TAG, "토큰 획득 취소됨 - RESULT_CANCELED 설정")
                setResult(RESULT_CANCELED)
            }
            is OAuthTokenResult.Failed -> {
                MSLog.e(TAG, "토큰 획득 실패 - RESULT_FAILED 설정", tokenResult.throwable)
                setResult(RESULT_FAILED)
            }
        }
        MSLog.d(TAG, "AuthActivity 종료")
        finish()
    }


    private fun signInGoogle() {
        MSLog.d(TAG, "Google Sign-In 프로세스 시작 (Credential Manager API)")
        viewModel.setLoadingPostpone(true)
        
        lifecycleScope.launch {
            try {
                MSLog.d(TAG, "Google Sign-In 시도")
                val tokenResult = googleClient.signIn(this@AuthActivity)
                viewModel.setLoadingPostpone(false)
                MSLog.d(TAG, "Google Sign-In 결과 처리")
                signInWithTokenResult(AuthProvider.GOOGLE, tokenResult)
            } catch (e: Exception) {
                MSLog.e(TAG, "Google Sign-In 중 예외 발생", e)
                viewModel.setLoadingPostpone(false)
                setResult(RESULT_FAILED)
                finish()
            }
        }
    }

    private suspend fun signInGuest() {
        MSLog.d(TAG, "Guest 로그인 프로세스 시작")
        runCatching {
            viewModel.requestGuestSignIn()
            MSLog.d(TAG, "Guest 로그인 성공 - RESULT_OK 설정")
            setResult(RESULT_OK)
        }.onFailure { exception ->
            MSLog.e(TAG, "Guest 로그인 실패 - RESULT_FAILED 설정", exception)
            setResult(RESULT_FAILED)
        }
        MSLog.d(TAG, "Guest 로그인 완료 - AuthActivity 종료")
        finish()
    }

    private suspend fun signOutClient() {
        when(BeepAuth.authInfo?.provider) {
            AuthProvider.GOOGLE -> {
                googleClient.signOut()
            }
            else -> Unit
        }
    }

    private suspend fun signOut() {
        runCatching {
            signOutClient()
            FirebaseAuth.getInstance().signOut()
            setResult(RESULT_OK)
        }.onFailure {
            setResult(RESULT_FAILED)
        }
        finish()
    }

    private suspend fun withdrawal() {
        runCatching {
            viewModel.withdrawalAndDeleteUserInfo()
            signOutClient()
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                currentUser.delete().await()
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_FAILED)
            }
        }.onFailure {
            setResult(RESULT_FAILED)
        }
        finish()
    }

    private fun showProgress() {
        show(ProgressDialog.TAG) {
            ProgressDialog.newInstance(ProgressParam(
                backgroundColor = getColor(ThemeR.color.black_30)
            ))
        }
    }

    private fun hideProgress() {
        dismiss(ProgressDialog.TAG)
    }
}