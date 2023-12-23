package com.lighthouse.beep.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.extension.mapJson
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.auth.network.RequestMethod
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.user.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
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

    suspend fun requestFirebaseSignInWithCustomToken(provider: AuthProvider, accessToken: String) : AuthResult {
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

        return Firebase.auth.signInWithCustomToken(firebaseToken).await()
    }

    suspend fun signOutAndChangeUserInfo() {
        userRepository.logout()
    }

    suspend fun withdrawalAndDeleteUserInfo() {
        gifticonRepository.deleteGifticon(BeepAuth.userUid)
        userRepository.logout()
    }
}