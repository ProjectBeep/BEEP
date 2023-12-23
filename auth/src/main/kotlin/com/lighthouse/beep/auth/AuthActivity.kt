package com.lighthouse.beep.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.client.GoogleClient
import com.lighthouse.beep.auth.client.KakaoClient
import com.lighthouse.beep.auth.client.NaverClient
import com.lighthouse.beep.auth.extension.mapJson
import com.lighthouse.beep.auth.model.OAuthTokenResult
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.auth.network.RequestMethod
import com.lighthouse.beep.model.user.AuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    companion object {
        const val RESULT_OK = 1001
        const val RESULT_CANCELED = 1002
        const val RESULT_FAILED = 1003
    }

    private val googleClient by lazy {
        GoogleClient(applicationContext)
    }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        lifecycleScope.launch {
            signInWithTokenResult(AuthProvider.GOOGLE, googleClient.getAccessToken(result))
        }
    }

    private val kakaoClient by lazy {
        KakaoClient(applicationContext)
    }

    private val naverClient by lazy {
        NaverClient(applicationContext)
    }

    private val naverAuthenticateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        lifecycleScope.launch {
            signInWithTokenResult(AuthProvider.NAVER, naverClient.getAccessToken(result))
        }
    }

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            when(val param = AuthParam.getParam(intent)) {
                is AuthParam.SignIn -> signIn(param.provider)
                is AuthParam.SignOut -> signOut()
                is AuthParam.Withdrawal -> withdrawal()
                is AuthParam.None -> finish()
            }
        }
    }

    private suspend fun signIn(provider: AuthProvider) {
        when(provider) {
            AuthProvider.NAVER -> signInNaver()
            AuthProvider.KAKAO -> signInKakao()
            AuthProvider.GOOGLE -> signInGoogle()
            AuthProvider.GUEST -> signInGuest()
            AuthProvider.NONE -> finish()
        }
    }

    private suspend fun signInWithTokenResult(provider: AuthProvider, tokenResult: OAuthTokenResult) {
        when (tokenResult) {
            is OAuthTokenResult.Success -> {
                runCatching {
                    val token = tokenResult.accessToken
                    viewModel.requestFirebaseSignInWithCustomToken(provider, token)
                    setResult(RESULT_OK)
                }.onFailure {
                    setResult(RESULT_FAILED)
                }
            }
            is OAuthTokenResult.Canceled -> {
                setResult(RESULT_CANCELED)
            }
            is OAuthTokenResult.Failed -> {
                setResult(RESULT_FAILED)
            }
        }
        finish()
    }

    private suspend fun signInNaver(tokenResult: OAuthTokenResult? = null) {
        if (tokenResult == null) {
            runCatching {
                signInNaver(naverClient.getAccessToken())
            }.onFailure {
                naverClient.authenticate(this, naverAuthenticateLauncher)
            }
        } else {
            signInWithTokenResult(AuthProvider.NAVER, tokenResult)
        }
    }

    private suspend fun signInKakao() {
        val tokenResult = kakaoClient.getAccessToken(this@AuthActivity)
        signInWithTokenResult(AuthProvider.KAKAO, tokenResult)
    }

    private fun signInGoogle() {
        googleSignInLauncher.launch(googleClient.signInIntent)
    }

    private suspend fun signInGuest() {
        runCatching {
            FirebaseAuth.getInstance().signInAnonymously().await()
            setResult(RESULT_OK)
        }.onFailure {
            setResult(RESULT_FAILED)
        }
        finish()
    }

    private suspend fun signOutAuthInfo() {
        when(BeepAuth.authInfo.provider) {
            AuthProvider.NAVER -> {
                naverClient.signOut()
            }
            AuthProvider.KAKAO -> {
                kakaoClient.signOut()
            }
            AuthProvider.GOOGLE -> {
                googleClient.signOut()
            }
            else -> Unit
        }
        FirebaseAuth.getInstance().signOut()
    }

    private suspend fun signOut() {
        runCatching {
            viewModel.signOutAndChangeUserInfo()
            signOutAuthInfo()
            setResult(RESULT_OK)
        }.onFailure {
            setResult(RESULT_FAILED)
        }
        finish()
    }

    private suspend fun withdrawal() {
        runCatching {
            viewModel.withdrawalAndDeleteUserInfo()
            signOutAuthInfo()
            val user = BeepAuth.currentUser
            if (user != null) {
                user.delete().await()
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_FAILED)
            }
        }.onFailure {
            setResult(RESULT_FAILED)
        }
        finish()
    }
}