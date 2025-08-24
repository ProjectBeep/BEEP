package com.lighthouse.beep.auth.data

import com.lighthouse.beep.auth.extension.mapJson
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.auth.network.RequestMethod
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.core.common.utils.log.MSLog
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import javax.inject.Inject

class RemoteAuthDataSource @Inject constructor(
    private val httpClient: OkHttpClient,
    private val networkTask: NetworkTask,
) {
    
    companion object {
        private const val TAG = "RemoteAuthDataSource"
        private const val PARAM_PROVIDER = "provider"
        private const val PARAM_ACCESS_TOKEN = "token"
        private const val RESPONSE_TOKEN = "token"
    }
    
    suspend fun getCustomToken(provider: AuthProvider, accessToken: String): String = 
        withContext(Dispatchers.IO) {
            MSLog.d(TAG, "Custom Token 요청 시작 - provider: $provider")
            MSLog.d(TAG, "Access Token: ${accessToken.take(20)}...")
            
            val request = NetworkRequest(
                method = RequestMethod.POST,
                url = BuildConfig.FIREBASE_CUSTOM_TOKEN_AUTH_URL,
                params = mapOf(
                    PARAM_PROVIDER to provider.name,
                    PARAM_ACCESS_TOKEN to accessToken,
                ),
            )
            MSLog.d(TAG, "네트워크 요청 생성 완료 - URL: ${BuildConfig.FIREBASE_CUSTOM_TOKEN_AUTH_URL}")
            
            runCatching {
                MSLog.d(TAG, "Custom Token 서버 요청 시작")
                val networkResult = networkTask.requestApi(request)
                MSLog.d(TAG, "Custom Token 서버 네트워크 요청 완료")
                
                val json = networkResult.mapJson().getOrThrow()
                MSLog.d(TAG, "Custom Token 서버 응답 JSON 파싱 성공")
                
                val firebaseToken = json.getString(RESPONSE_TOKEN)
                MSLog.d(TAG, "Firebase Token 추출 완료: ${firebaseToken.take(20)}...")
                firebaseToken
            }.getOrElse { exception ->
                when (exception) {
                    is IllegalStateException -> {
                        MSLog.e(TAG, "Custom Token 서버 HTTP 오류: ${exception.message}", exception)
                    }
                    else -> {
                        MSLog.e(TAG, "Custom Token 요청 실패 - ${exception.javaClass.simpleName}: ${exception.message}", exception)
                    }
                }
                throw exception
            }
        }
}