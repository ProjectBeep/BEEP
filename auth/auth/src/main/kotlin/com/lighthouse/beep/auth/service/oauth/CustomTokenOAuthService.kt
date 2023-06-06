package com.lighthouse.beep.auth.service.oauth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.auth.extension.mapJson
import com.lighthouse.beep.auth.model.OAuthRequest
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.auth.network.RequestMethod

internal class CustomTokenOAuthService(
    private val task: NetworkTask,
) : OAuthService {

    companion object {
        private const val PARAM_PROVIDER = "provider"
        private const val PARAM_ACCESS_TOKEN = "token"

        private const val RESPONSE_RESULT = "result"
        private const val RESPONSE_TOKEN = "token"
    }

    override suspend fun signIn(oAuthRequest: OAuthRequest): Task<AuthResult> {
        val request = NetworkRequest(
            method = RequestMethod.POST,
            url = BuildConfig.FIREBASE_CUSTOM_TOKEN_AUTH_URL,
            params = mapOf(
                PARAM_PROVIDER to oAuthRequest.provider.name,
                PARAM_ACCESS_TOKEN to oAuthRequest.accessToken,
            ),
        )

        val firebaseToken = task.requestApi(request)
            .mapJson()
            .getOrThrow()
            .getJSONObject(RESPONSE_RESULT)
            .getString(RESPONSE_TOKEN)

        return Firebase.auth.signInWithCustomToken(firebaseToken)
    }
}
