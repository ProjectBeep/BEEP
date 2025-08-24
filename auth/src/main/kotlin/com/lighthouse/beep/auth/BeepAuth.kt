package com.lighthouse.beep.auth

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.UserProfileChangeRequest
import com.lighthouse.beep.core.common.utils.log.MSLog
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

object BeepAuth {

    private const val TAG = "BeepAuth"

    fun getSignInIntent(context: Context, provider: AuthProvider): Intent {
        MSLog.d(TAG, "getSignInIntent - provider: $provider")
        return AuthParam.SignIn(provider).getIntent(context)
    }

    fun getSignOutIntent(context: Context): Intent {
        return AuthParam.SignOut.getIntent(context)
    }

    fun getWithdrawalIntent(context: Context): Intent {
        return AuthParam.Withdrawal.getIntent(context)
    }

    private val _authInfoFlow = MutableStateFlow<AuthInfo?>(null)
    val authInfoFlow = _authInfoFlow.asStateFlow()

    val currentUid = authInfoFlow.filterNotNull().map { it.userUid }

    init {
        MSLog.d(TAG, "Firebase Auth 상태 리스너 초기화")
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            MSLog.d(TAG, "Firebase Auth 상태 변경 - 사용자: ${if (currentUser == null) "null" else "exists(${currentUser.uid})"}")
            if (currentUser == null) {
                MSLog.d(TAG, "사용자 정보가 없어 AuthInfo.Default 설정")
                _authInfoFlow.value = AuthInfo.Default
            } else {
                MSLog.d(TAG, "사용자 ID Token 요청 시작 - uid: ${currentUser.uid}")
                currentUser.getIdToken(true).addOnSuccessListener { result ->
                    val info = getAuthInfo(currentUser, result)
                    MSLog.d(TAG, "ID Token 획득 성공 - AuthInfo 업데이트: provider=${info.provider}, uid=${info.userUid}")
                    _authInfoFlow.value = info
                }.addOnFailureListener { exception ->
                    MSLog.e(TAG, "ID Token 획득 실패", exception)
                }
            }
        }
    }

    private fun getAuthInfo(
        user: FirebaseUser,
        result: GetTokenResult
    ): AuthInfo {
        MSLog.d(TAG, "getAuthInfo 호출 - uid: ${user.uid}, isAnonymous: ${user.isAnonymous}")
        
        val provider = runCatching {
            if (user.isAnonymous) {
                MSLog.d(TAG, "익명 사용자로 GUEST 프로바이더 설정")
                AuthProvider.GUEST
            } else {
                val providerName = result.claims["provider"] as? String ?: ""
                MSLog.d(TAG, "프로바이더 이름 추출: '$providerName'")
                AuthProvider.of(providerName)
            }
        }.getOrElse { exception ->
            MSLog.e(TAG, "프로바이더 정보 추출 실패 - NONE으로 설정", exception)
            AuthProvider.NONE
        }

        val authInfo = AuthInfo(
            userUid = user.uid,
            provider = provider,
            displayName = user.displayName ?: "",
            email = user.email ?: "",
            photoUrl = user.photoUrl,
        )
        
        MSLog.d(TAG, "AuthInfo 생성 완료 - provider: ${authInfo.provider}, displayName: '${authInfo.displayName}', email: '${authInfo.email}'")
        return authInfo
    }

    suspend fun updateProfile(request: UserProfileChangeRequest) {
        MSLog.d(TAG, "updateProfile 호출")
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            MSLog.d(TAG, "프로필 업데이트 시작 - uid: ${currentUser.uid}")
            runCatching {
                currentUser.updateProfile(request).await()
                MSLog.d(TAG, "프로필 업데이트 완료")
                val result = currentUser.getIdToken(false).await()
                MSLog.d(TAG, "업데이트된 ID Token 획득 완료")
                _authInfoFlow.value = getAuthInfo(currentUser, result)
                MSLog.d(TAG, "AuthInfo 업데이트 완료")
            }.onFailure { exception ->
                MSLog.e(TAG, "프로필 업데이트 실패", exception)
            }
        } else {
            MSLog.w(TAG, "updateProfile - 현재 사용자가 null입니다")
        }
    }

    val authInfo: AuthInfo?
        get() = authInfoFlow.value

    val authProvider: AuthProvider
        get() = authInfo?.provider ?: AuthProvider.NONE

    val userUid: String
        get() = authInfo?.userUid ?: ""
}