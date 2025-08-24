package com.lighthouse.beep.auth.client

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.auth.model.OAuthTokenResult
import com.lighthouse.beep.core.common.utils.log.MSLog

internal class GoogleClient(private val context: Context) {
    companion object {
        private const val TAG = "GoogleClient"
    }

    // Credential Manager - Android 14+ 권장 방식
    private val credentialManager by lazy {
        MSLog.d(TAG, "Credential Manager 초기화")
        CredentialManager.create(context)
    }

    // Google Sign-In - Credential Manager API 사용
    suspend fun signIn(activity: Activity): OAuthTokenResult {
        MSLog.d(TAG, "Google Sign-In 시작 (Credential Manager API)")
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false) // 모든 계정 표시
                .setAutoSelectEnabled(false) // 자동 선택 비활성화
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            MSLog.d(TAG, "Credential Manager 요청 생성 완료")
            val result = credentialManager.getCredential(activity, request)
            handleCredentialManagerResult(result)
        } catch (e: GetCredentialException) {
            MSLog.e(TAG, "Credential Manager 로그인 실패", e)
            when (e) {
                is androidx.credentials.exceptions.GetCredentialCancellationException -> {
                    MSLog.d(TAG, "사용자가 로그인을 취소함")
                    OAuthTokenResult.Canceled()
                }
                else -> {
                    MSLog.e(TAG, "Credential Manager 오류: ${e.message}")
                    OAuthTokenResult.Failed(e)
                }
            }
        }
    }

    private fun handleCredentialManagerResult(response: GetCredentialResponse): OAuthTokenResult {
        MSLog.d(TAG, "Credential Manager 응답 처리 시작")
        return try {
            val credential = GoogleIdTokenCredential.createFrom(response.credential.data)
            val idToken = credential.idToken
            
            MSLog.d(TAG, "Google ID Token 추출 성공: ${idToken.take(20)}...")
            MSLog.d(TAG, "사용자 정보 - ID: ${credential.id}, Name: ${credential.displayName}")
            
            OAuthTokenResult.Success(idToken)
        } catch (e: Exception) {
            MSLog.e(TAG, "Credential 데이터 파싱 실패", e)
            OAuthTokenResult.Failed(e)
        }
    }


    suspend fun signOut() {
        MSLog.d(TAG, "Google Sign-Out 시작")
        // Credential Manager는 별도 Sign-Out이 필요하지 않음
        // Google 계정은 시스템 레벨에서 관리됨
        MSLog.d(TAG, "Google Sign-Out 완료 (Credential Manager 방식)")
    }

}
