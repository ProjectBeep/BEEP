package com.lighthouse.features.intro.ui

import com.lighthouse.beep.library.network.NetworkRequest
import com.lighthouse.beep.library.network.NetworkTask
import com.lighthouse.beep.library.network.RequestMethod
import com.lighthouse.beep.library.network.mapJson
import com.lighthouse.beep.ui.page.intro.BuildConfig
import okhttp3.OkHttpClient
import javax.inject.Inject

class FirebaseAuthWithKakaoApi @Inject constructor(
    okHttpClient: OkHttpClient,
) : NetworkTask(okHttpClient) {

    companion object {
        private const val RESULT = "result"
        private const val RESULT_TOKEN = "token"
    }

    suspend fun request(param: Param): Result<String> = runCatching {
        val request = NetworkRequest(
            method = RequestMethod.POST,
            url = BuildConfig.FIREBASE_KAKAO_AUTH_URL,
            params = param.buildParams()
        )

        requestApi(request)
            .mapJson()
            .getOrThrow()
            .getJSONObject(RESULT)
            .getString(RESULT_TOKEN)
    }

    data class Param(
        val token: String,
    ) {
        fun buildParams(): Map<String, String> {
            return mapOf(
                PARAM_ACCESS_TOKEN to token
            )
        }

        companion object {
            private const val PARAM_ACCESS_TOKEN = "token"
        }
    }
}