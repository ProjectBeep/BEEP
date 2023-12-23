package com.lighthouse.beep.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.extension.mapJson
import com.lighthouse.beep.auth.mapper.toAuthInfo
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.auth.network.RequestMethod
import com.lighthouse.beep.core.common.utils.flow.MutableEventFlow
import com.lighthouse.beep.core.common.utils.flow.asEventFlow
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.user.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    companion object {
        private const val PARAM_PROVIDER = "provider"
        private const val PARAM_ACCESS_TOKEN = "token"

        private const val RESPONSE_TOKEN = "token"
    }

    private val _loadingPostpone = MutableEventFlow<Boolean>(replay = 1)
    val loadingPostpone = _loadingPostpone.asEventFlow()

    fun setLoadingPostpone(value: Boolean) {
        viewModelScope.launch {
            _loadingPostpone.emit(value)
        }
    }

    suspend fun requestFirebaseSignInWithCustomToken(
        provider: AuthProvider,
        accessToken: String
    ): AuthResult = withContext(Dispatchers.IO) {
        val request = NetworkRequest(
            method = RequestMethod.POST,
            url = BuildConfig.FIREBASE_CUSTOM_TOKEN_AUTH_URL,
            params = mapOf(
                PARAM_PROVIDER to provider.name,
                PARAM_ACCESS_TOKEN to accessToken,
            ),
        )

        val client = OkHttpClient.Builder().build()

        val json = NetworkTask(client).requestApi(request)
            .mapJson()
            .getOrThrow()

        val firebaseToken = json.getString(RESPONSE_TOKEN)

        Firebase.auth.signInWithCustomToken(firebaseToken).await()
    }

    suspend fun requestGuestSignIn() = withContext(Dispatchers.IO) {
        val result = FirebaseAuth.getInstance().signInAnonymously().await()
        val authInfo = result.user?.toAuthInfo()

        if (authInfo != null) {
            userRepository.setAuthInfo { authInfo }
        }
    }

    suspend fun signOutAndChangeUserInfo() = withContext(Dispatchers.IO) {
        userRepository.logout()
    }

    suspend fun withdrawalAndDeleteUserInfo() = withContext(Dispatchers.IO) {
        gifticonRepository.deleteGifticon(BeepAuth.userUid)
        userRepository.logout()
    }
}